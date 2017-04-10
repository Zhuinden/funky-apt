package com.zhuinden;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.*;

/**
 * Created by Owner on 2017. 04. 10..
 */
@AutoService(Processor.class)
public class MyProcessor
    extends AbstractProcessor {
    ProcessingEnvironment processingEnvironment;
    Elements elements;
    Messager messager;
    Filer filer;
    Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.processingEnvironment = processingEnv;
        this.filer = processingEnvironment.getFiler();
        this.elements = processingEnvironment.getElementUtils();
        this.messager = processingEnvironment.getMessager();
        this.types = processingEnvironment.getTypeUtils();
    }

    /*
         public static class Builder {
            private String hello;
            private String world;

            public Builder setHello(String hello) {
                this.hello = hello;
                return this;
            }

            public Builder setWorld(String world) {
                this.world = world;
                return this;
            }

            public App build() {
                App app = new App();
                app.hello = hello;
                app.world = world;
                return app;
            }
         }
     */

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(Funk.class);
        for(Element element : annotatedElements) {
            messager.printMessage(Diagnostic.Kind.NOTE, "Element traversed: " + element.asType(), element);
            TypeMirror typeMirror = element.asType();
            String className = typeMirror.toString(); // com.zhuinden.App
            String simpleName = element.getSimpleName().toString(); // App
            String packageName = className.substring(0, className.lastIndexOf('.')); // com.zhuinden
            messager.printMessage(Diagnostic.Kind.NOTE, "Package Name [" + packageName + "], Class Name [" + className + "], Simple Name [" + simpleName + "]");
            try {
                String builderSimpleName = simpleName + "Builder";
                TypeSpec.Builder builder = TypeSpec.classBuilder(builderSimpleName);
                builder.addModifiers(Modifier.PUBLIC, Modifier.FINAL);
                ClassName builderClassName = ClassName.get(packageName, builderSimpleName);
                List<String> fieldNames = new ArrayList<>();
                for(Element enclosedElement : element.getEnclosedElements()) {
                    if(enclosedElement.getKind() == ElementKind.FIELD) {
                        Set<Modifier> modifiers = enclosedElement.getModifiers();
                        if(!modifiers.contains(Modifier.STATIC) && !modifiers.contains(Modifier.PRIVATE)) { /* LIMITATION: private fields cannot be accessed by external class */
                            TypeName fieldType = TypeName.get(enclosedElement.asType());
                            String fieldName = enclosedElement.getSimpleName().toString();
                            fieldNames.add(fieldName);
                            builder.addField(FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE).build());
                            builder.addMethod(MethodSpec.methodBuilder("set" + StringUtils.capitalize(fieldName)) //
                                .addModifiers(Modifier.PUBLIC).addParameter(fieldType, fieldName) //
                                .returns(builderClassName) //
                                .addStatement("this.$L = $L", fieldName, fieldName) //
                                .addStatement("return this") //
                                .build()); //
                        }
                    }
                }
                String enclosingAsVariable = StringUtils.uncapitalize(simpleName);
                MethodSpec.Builder methodSpecBuilder = MethodSpec.methodBuilder("build") //
                    .addModifiers(Modifier.PUBLIC) //
                    .returns(TypeName.get(typeMirror)) //
                    .addStatement("$L $L = new $L()", simpleName, enclosingAsVariable, simpleName);
                for(String fieldName : fieldNames) {
                    methodSpecBuilder.addStatement("$L.$L = this.$L", enclosingAsVariable, fieldName, fieldName);
                }
                methodSpecBuilder.addStatement("return $L", enclosingAsVariable);
                builder.addMethod(methodSpecBuilder.build());
                JavaFile javaFile = JavaFile.builder(packageName, builder.build()).build();
                javaFile.writeTo(filer);
            } catch(IOException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, "FAILED TO WRITE FILE FOR [" + className + "]");
                e.printStackTrace();
                return true;
            }
        } return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new HashSet<>();
        annotations.add(Funk.class.getName());
        return Collections.unmodifiableSet(annotations);
    }
}
