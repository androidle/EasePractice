package com.leapp.yangle.arouter.compiler;

import com.google.auto.service.AutoService;
import com.leapp.yangle.arouter.annotations.ARouter;
import com.leapp.yangle.arouter.annotations.model.RouterBean;
import com.leapp.yangle.arouter.compiler.utils.Constants;
import com.leapp.yangle.arouter.compiler.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

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
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

@AutoService(Processor.class)//register processor
@SupportedAnnotationTypes(Constants.AROUTER_ANNOTATION_TYPES)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({Constants.MODULE_NAME,Constants.APT_PACKAGE})
public class ARouterProcessor extends AbstractProcessor {


    private Elements mElementUtils;
    private Messager mMessager;
    private Filer mFiler;
    private Types mTypeUtils;

    private String mModuleName;
    private String mPackageNameForApt;

    private Map<String, List<RouterBean>> mTempPathMap = new HashMap<>();
    private Map<String, String> mTempGroupMap = new HashMap<>();
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        mTypeUtils = processingEnv.getTypeUtils();

        Map<String, String> options = processingEnv.getOptions();
        if (!EmptyUtils.isEmpty(options)) {
            mModuleName = options.get(Constants.MODULE_NAME);
            mPackageNameForApt = options.get(Constants.APT_PACKAGE);

            // 不用Diagnostic.Kind.ERROR,会抛异常
            mMessager.printMessage(Diagnostic.Kind.NOTE,"moduleName====>"+ mModuleName);
            mMessager.printMessage(Diagnostic.Kind.NOTE,"packageName====>"+ mPackageNameForApt);
        }

        if (EmptyUtils.isEmpty(mModuleName) || EmptyUtils.isEmpty(mPackageNameForApt)) {
            throw new RuntimeException("注解处理器的参数moduleName 或者packageName 为空,请在对应gradle配置参数");
        }


    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (!EmptyUtils.isEmpty(set)) {
            Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
            if (!EmptyUtils.isEmpty(elements)) {
                try {
                    parseElements(elements);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;
        }
        return true;
    }

    private void parseElements(Set<? extends Element> elements) throws IOException {

        TypeElement activityType = mElementUtils.getTypeElement(Constants.ACTIVITY);
        TypeMirror activityMirror = activityType.asType();

        for (Element element : elements) {
            TypeMirror typeMirror = element.asType();
            mMessager.printMessage(Diagnostic.Kind.NOTE,"遍历的元素信息为====>"+ typeMirror.toString());

            // 获取每个类的上ARouter 注解
            ARouter aRouter = element.getAnnotation(ARouter.class);

            // 封装路由实体信息
            RouterBean routerBean = new RouterBean.Builder()
                    .setGroup(aRouter.group())
                    .setPath(aRouter.path())
                    .setElement(element)
                    .build();

            if (mTypeUtils.isSubtype(typeMirror, activityMirror)) {
                routerBean.setType(RouterBean.Type.ACTIVITY);
            } else {
                throw new RuntimeException("@ARouter only support Activity currently");
            }

            // 临时map存储以上信息,用来遍历生成代码
            valueOfPathMap(routerBean);
        }

        // ARouterLoadPath&ARouterLoadGroup类型,用来生成类文件时的实现接口
        TypeElement groupLoadType = mElementUtils.getTypeElement(Constants.AROUTER_GROUP);
        TypeElement pathLoadType = mElementUtils.getTypeElement(Constants.AROUTER_PATH);

        // 1.create path class,eg:ARouter$$Path$$app
        createPathFile(pathLoadType);
        // 2.create group class,eg:ARouter$$Group$app
        createGroupFile(pathLoadType, groupLoadType);

    }

    private void createPathFile(TypeElement pathLoadType) throws IOException {
        if (EmptyUtils.isEmpty(mTempPathMap)) return;

        // return value Map<String, RouterBean>
        TypeName methodReturn = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class));

        // 遍历分组,每一个分组创建一个path类文件,ARouter$$Path$$app
        for (Map.Entry<String, List<RouterBean>> entry : mTempPathMap.entrySet()) {
            //方法体构造 public Map<String, RouterBean> loadPath()
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.PATH_METHOD_NAME)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(methodReturn);

            // 不循环部分Map<String, RouterBean> pathMap = new HashMap<>();
            methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    Constants.PATH_PARAMETER_NAME,
                    HashMap.class);

            // pathMap.put("/app/MainActivity",...
            // 方法内容的循环部分
            List<RouterBean> pathList = entry.getValue();
            for (RouterBean routerBean : pathList) {
                /**
                 * pathMap.put("/app/MainActivity",
                 *                     RouterBean.create(RouterBean.Type.ACTIVITY,
                 *                             MainActivity.class,
                 *                             "/app/MainActivity", "app"));
                 */

                methodBuilder.addStatement(
                        "$N.put($S,$T.create($T.$L,$T.class,$S,$S))",
                        Constants.PATH_PARAMETER_NAME,//pathMap.put
                        routerBean.getPath(),//"/app/MainActivity"
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.Type.class),
                        routerBean.getType(),//ACTIVITY
                        ClassName.get((TypeElement) routerBean.getElement()),// MainActivity.class,
                        routerBean.getPath(),//"/app/MainActivity"
                        routerBean.getGroup()// "app"
                        );

            }

            // 遍历过后,return pathMap;
            methodBuilder.addStatement("return $N", Constants.PATH_PARAMETER_NAME);

            // 生成类文件
            String finalClassName = Constants.PATH_FILE_NAME + entry.getKey();
            mMessager.printMessage(Diagnostic.Kind.NOTE,"APT生成路由的path类文件为:"
                    + mPackageNameForApt + "." + finalClassName);

            JavaFile.builder(mPackageNameForApt,//包路径
                    TypeSpec.classBuilder(finalClassName)//类名
                            .addSuperinterface(ClassName.get(pathLoadType))//实现接口
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(methodBuilder.build())// 添加方法
                            .build()
            )
                    .build()
                    .writeTo(mFiler);

            mTempGroupMap.put(entry.getKey(), finalClassName);

        }

    }

    private void createGroupFile(TypeElement pathLoadType, TypeElement groupLoadType) throws IOException {
        if (EmptyUtils.isEmpty(mTempGroupMap) || mTempPathMap.isEmpty()) return;

        //Map<String, Class<? extends ARouterLoadPath>>
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                //Class<? extends ARouterLoadPath>>
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadType)))

        );
        // 方法
        //public Map<String, Class<? extends ARouterLoadPath>> loadGroup()
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.GROUP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturns);
        // 遍历之前Map<String, Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
        methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadType))),
                Constants.GROUP_PARAMETER_NAME,
                HashMap.class);

        // 方法体循环部分
        for (Map.Entry<String, String> entry : mTempGroupMap.entrySet()) {
            //groupMap.put("app", ARouter$$Path$$App.class);
            methodBuilder.addStatement("$N.put($S,$T.class)",
                    Constants.GROUP_PARAMETER_NAME,
                    entry.getKey(),
                    ClassName.get(mPackageNameForApt, entry.getValue()))
            ;
        }

        // 返回值
        methodBuilder.addStatement("return $N", Constants.GROUP_PARAMETER_NAME);

        // 生成文件的名称
        String finalClassName = Constants.GROUP_FILE_NAME + mModuleName;
        mMessager.printMessage(Diagnostic.Kind.NOTE,"APT生成路由的Group类文件为:"
                + mPackageNameForApt + "." + finalClassName);

        // 生成类文件
        JavaFile.builder(mPackageNameForApt,//包路径
                TypeSpec.classBuilder(finalClassName)//类名
                        .addSuperinterface(ClassName.get(groupLoadType))//实现接口
                        .addModifiers(Modifier.PUBLIC)
                        .addMethod(methodBuilder.build())// 添加方法
                        .build()
        )
                .build()
                .writeTo(mFiler);

    }

    private void valueOfPathMap(RouterBean routerBean) {
        if (checkRouterBean(routerBean)) {
            mMessager.printMessage(Diagnostic.Kind.NOTE, "RouterBean====>" + routerBean.toString());

            List<RouterBean> routerBeans = mTempPathMap.get(routerBean.getGroup());
            if (EmptyUtils.isEmpty(routerBeans)) {
                routerBeans = new ArrayList<>();
                routerBeans.add(routerBean);
                mTempPathMap.put(routerBean.getGroup(), routerBeans);
            } else {
                routerBeans.add(routerBean);
            }
        } else {
            mMessager.printMessage(Diagnostic.Kind.ERROR,
                    "@ARouter Annotation is not correct like /app/MainActivity");
        }
    }

    private boolean checkRouterBean(RouterBean routerBean) {
        String group = routerBean.getGroup();
        String path = routerBean.getPath();
        if (EmptyUtils.isEmpty(path) || !path.startsWith("/")) {
            mMessager.printMessage(Diagnostic.Kind.ERROR,
                    "@ARouter Annotation is not correct like /app/MainActivity");
            return false;
        }

        // /MainActivity
        if (path.lastIndexOf("/") == 0) {
            mMessager.printMessage(Diagnostic.Kind.ERROR,
                    "@ARouter Annotation is not correct like /app/MainActivity");
            return false;
        }

        String finalGroup = path.substring(1, path.indexOf("/", 1));
        // /MainActivity/MainActivity/MainActivity
        if (finalGroup.contains("/")) {
            mMessager.printMessage(Diagnostic.Kind.ERROR,
                    "@ARouter Annotation is not correct like /app/MainActivity");
            return false;
        }

        if (!EmptyUtils.isEmpty(group) && !group.equals(mModuleName)) {
            mMessager.printMessage(Diagnostic.Kind.ERROR,
                    "@ARouter group must be same as the moduleName");
            return false;
        } else {
            routerBean.setGroup(finalGroup);
        }

        return true;
    }
}
