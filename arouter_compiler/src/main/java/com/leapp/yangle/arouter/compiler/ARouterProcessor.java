package com.leapp.yangle.arouter.compiler;

import com.leapp.yangle.arouter.annotations.ARouter;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

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
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> supportTypes = new LinkedHashSet<>();
        supportTypes.add(ARouter.class.getCanonicalName());
        return supportTypes;
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> supportOptions = new LinkedHashSet();
        supportOptions.add("content");
        return supportOptions;
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

            // basic contact string（https://github.com/greenrobot/EventBus）
            try {
                JavaFileObject sourceFile = mFiler.createSourceFile(packageName + "." + finalClassName);
                Writer writer = sourceFile.openWriter();
                writer.write("package " + packageName + ";\n");
                writer.write("public class " + finalClassName + " {\n");
                writer.write("public static Class<?> findTargetClass(String path) {\n");
                ARouter aRouter = element.getAnnotation(ARouter.class);
                writer.write("if (path.equals(\"" + aRouter.path() + "\")) {\n");
                writer.write("return " + className + ".class;\n}\n");
                writer.write("return null;\n");
                writer.write("}\n}");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
