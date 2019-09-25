package com.leapp.yangle.arouter.compiler;

import com.google.auto.service.AutoService;
import com.leapp.yangle.arouter.annotations.Parameter;
import com.leapp.yangle.arouter.compiler.factory.ParameterFactory;
import com.leapp.yangle.arouter.compiler.utils.Constants;
import com.leapp.yangle.arouter.compiler.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes(Constants.PARAMETER_ANNOTATION_TYPES)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({Constants.MODULE_NAME,Constants.APT_PACKAGE})
public class ParameterProcessor extends AbstractProcessor {


    private Elements mElementUtils;
    private Messager mMessager;
    private Filer mFiler;
    private Types mTypeUtils;

    private Map<TypeElement, List<Element>> mTempParameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        mTypeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!EmptyUtils.isEmpty(set)) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Parameter.class);
            if (!EmptyUtils.isEmpty(elements)) {
                valueOfParameterMap(elements);
                try {
                    createParameterFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    private void valueOfParameterMap(Set<? extends Element> elements) {
        for (Element element : elements) {
            // 注解的属性,父节点的类节点
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            if (mTempParameterMap.containsKey(typeElement)) {
                mTempParameterMap.get(typeElement).add(element);
            } else {
                List<Element> fields = new ArrayList<>();
                fields.add(element);
                mTempParameterMap.put(typeElement, fields);
            }
        }
    }

    private void createParameterFile() throws IOException {
        if (EmptyUtils.isEmpty(mTempParameterMap)) return;

        TypeElement parameterType = mElementUtils.getTypeElement(Constants.PARAMETER_LOAD);

        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, Constants.PARAMETER_NAME).build();

        for (Map.Entry<TypeElement, List<Element>> entry : mTempParameterMap.entrySet()) {
            // key为类名,如MainActivity
            TypeElement typeElement = entry.getKey();
            // 获取类名
            ClassName className = ClassName.get(typeElement);
            // 方法体内容构建
            ParameterFactory factory = new ParameterFactory.Builder(parameterSpec)
                    .setMessager(mMessager)
                    .setElementUtils(mElementUtils)
                    .setTypeUtils(mTypeUtils)
                    .setClassName(className)
                    .build();

            factory.addFirstStatement();

            // 遍历类中的所有属性
            for (Element FieldElement : entry.getValue()) {
                factory.buildStatement(FieldElement);
            }


            // 最终生成的类文件,(MainActivity$$Parameter)
            String finalClassName = typeElement.getSimpleName() + Constants.PARAMETER_FILE_NAME;
            mMessager.printMessage(Diagnostic.Kind.NOTE,"APT生成获取参数类文件为:"
                    + className.packageName() + "." + finalClassName);

            JavaFile.builder(className.packageName(),
                    TypeSpec.classBuilder(finalClassName)
                            .addSuperinterface(ClassName.get(parameterType))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(factory.build())
                            .build()
                    ).build()
                    .writeTo(mFiler);

        }
    }

}
