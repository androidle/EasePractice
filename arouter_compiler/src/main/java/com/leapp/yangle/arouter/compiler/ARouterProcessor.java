package com.leapp.yangle.arouter.compiler;

import com.google.auto.service.AutoService;
import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.arouter.compiler.utils.Constants;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
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

@AutoService(Processor.class)//register processor
@SupportedAnnotationTypes(Constants.AROUTER_ANNOTATION_TYPES)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions("content")
public class ARouterProcessor extends AbstractProcessor {


    private Elements mElementUtils;
    private Messager mMessager;
    private Filer mFiler;
    private Types mTypeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        mTypeUtils = processingEnv.getTypeUtils();

        String content = processingEnv.getOptions().get("content");
        mMessager.printMessage(Diagnostic.Kind.NOTE,"注解参数为====>"+ content);

    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("============process start===开始============");
        if (set.isEmpty()) return false;
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        for (Element element : elements) {
            String packageName = mElementUtils.getPackageOf(element).getQualifiedName().toString();
            String className = element.getSimpleName().toString();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "被注解的类有：" + className);
            String finalClassName = className + "$$ARouter";
            try {

                ARouter aRouter = element.getAnnotation(ARouter.class);

                // user javaPoet
                MethodSpec method = MethodSpec.methodBuilder("findTargetClass")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(Class.class) // 返回值Class<?>
                        .addParameter(String.class, "path")
                        // return path.equals("/app/MainActivity") ? MainActivity.class : null
                        .addStatement("return path.equals($S) ? $T.class : null",
                                aRouter.path(), ClassName.get((TypeElement) element))
                        .build(); // 构建

                TypeSpec type = TypeSpec.classBuilder(finalClassName)
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(method)
                        .build();

                JavaFile javaFile = JavaFile.builder(packageName, type)
                        .build();

                javaFile.writeTo(System.out);
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }
}
