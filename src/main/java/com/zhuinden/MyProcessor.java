package com.zhuinden;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Owner on 2017. 04. 10..
 */
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {
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

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Funk.class);
        for(Element element : elements) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Element traversed", element);
        }
        return false;
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
