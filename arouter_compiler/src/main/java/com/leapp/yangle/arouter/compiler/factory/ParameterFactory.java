package com.leapp.yangle.arouter.compiler.factory;

import com.leapp.yangle.arouter.annotations.Parameter;
import com.leapp.yangle.arouter.compiler.utils.Constants;
import com.leapp.yangle.arouter.compiler.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class ParameterFactory {

    // MainActivity t = (MainActivity) target;
    private static final String CONTENT = "$T t = ($T)target";

    private Messager messager;
    private Types typesUtils;
    private ClassName className;

    private MethodSpec.Builder methodBuilder;
    private TypeMirror callMirror;

    private ParameterFactory() {
    }

    private ParameterFactory(Builder builder) {
        this.messager = builder.messager;
        this.typesUtils = builder.typesUtils;
        this.className = builder.className;

        methodBuilder = MethodSpec.methodBuilder(Constants.PARAMETER_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(builder.parameterSpec);

        this.callMirror = builder.elementUtils
                .getTypeElement(Constants.CALL)
                .asType();
    }

    public void addFirstStatement() {
        methodBuilder.addStatement(CONTENT, className, className);
    }

    public void buildStatement(Element fieldElement) {
        TypeMirror typeMirror = fieldElement.asType();
        // 获取 TypeKind 枚举类开序列号
        int type = typeMirror.getKind().ordinal();

        String fieldName = fieldElement.getSimpleName().toString();

        String annotationValue = fieldElement.getAnnotation(Parameter.class).name();

        annotationValue = EmptyUtils.isEmpty(annotationValue) ? fieldName : annotationValue;

        String finalValue = "t." + fieldName;

        String methodContent = finalValue + " = t.getIntent().";

        //  TypeKind 枚举类不包含string
        if (type == TypeKind.INT.ordinal()) {
            //t.age = t.getIntent().getIntExtra("age",t.age);
            methodContent += "getIntExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.BOOLEAN.ordinal()) {
            methodContent += "getBooleanExtra($S, " + finalValue + ")";
        } else {
            if (typeMirror.toString().equalsIgnoreCase(Constants.STRING)) {
                methodContent += "getStringExtra($S)";
            } else if(typesUtils.isSubtype(typeMirror,callMirror)) {
                // t.iUser = (IUserImpl)RouterManager.getInstance().build("/order/getOrderInfo").navigation(t)
                methodContent = "t." + fieldName + " =($T)$T.getInstance().build($S).navigation(t)";
                methodBuilder.addStatement(methodContent,
                        TypeName.get(typeMirror),
                        ClassName.get(Constants.BASE_PACKAGE, Constants.ROUTER_MANAGER),
                        annotationValue);

                return;
            }
        }

        if (methodContent.endsWith(")")) {
            methodBuilder.addStatement(methodContent, annotationValue);
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "目前暂支持Int,boolean,String传参");
        }
    }

    public MethodSpec build() {
        return this.methodBuilder.build();
    }

    public static final class Builder{

        private Messager messager;
        private Types typesUtils;
        private Elements elementUtils;
        private ClassName className;
        private final ParameterSpec parameterSpec;

        public Builder(ParameterSpec parameterSpec) {
            this.parameterSpec = parameterSpec;
        }


        public Builder setMessager(Messager messager) {
            this.messager = messager;
            return this;
        }

        public Builder setTypeUtils(Types typesUtils) {
            this.typesUtils = typesUtils;
            return this;
        }

        public Builder setClassName(ClassName className) {
            this.className = className;
            return this;
        }

        public Builder setElementUtils(Elements elementUtils) {
            this.elementUtils = elementUtils;
            return this;
        }

        public ParameterFactory build() {
            if (parameterSpec == null) {
                throw new IllegalArgumentException("parameterSpec == null");
            }

            if (className == null) {
                throw new IllegalArgumentException("className == null");
            }

            if (messager == null) {
                throw new IllegalArgumentException("messager == null");
            }

            return new ParameterFactory(this);
        }

    }
}
