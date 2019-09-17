package com.leapp.yangle.module.compiler;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.leapp.yangle.module.annotation.ARouter")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedOptions("content")
public class ARouterProcessor extends AbstractProcessor {

    private Elements elementsUtils;

    // class info
    private Types typesUtils;

    // output warning and error log
    private Messager messageUtils;

    // file creator
    private Filer filerUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        elementsUtils = processingEnvironment.getElementUtils();
        typesUtils = processingEnvironment.getTypeUtils();
        messageUtils = processingEnvironment.getMessager();
        filerUtils = processingEnvironment.getFiler();

        // getOption result and print it
        String content = processingEnvironment.getOptions().get("content");
        messageUtils.printMessage(Diagnostic.Kind.NOTE,content);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        System.out.println("==================process===================================");

        MethodSpec main = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .addStatement("$T.out.println($S)", System.class, "Hello, JavaPoet!")
                .build();
        TypeSpec helloWorld = TypeSpec.classBuilder("HelloWorld")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(main)
                .build();
        JavaFile javaFile = JavaFile.builder("com.leapp.yangle.practice", helloWorld)
                .build();

        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
