package com.sanchezjm.tuto.javassist.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class JavassistRunListener implements SpringApplicationRunListener {

    private static boolean isExecuted; // because can be executed twice, in different instances, is static

    private final SpringApplication application;

    public JavassistRunListener(SpringApplication application, String[] args) {
        this.application = application;
    }
    
    public void markAsIllegalArgumentException() {
		
		try {
			final ClassPool pool = ClassPool.getDefault();
			pool.appendClassPath(new LoaderClassPath(application.getClassLoader()));
			final CtClass cc = pool.get("com.sanchezjm.tuto.javassist.HelloWorld");
			final CtMethod m = cc.getDeclaredMethod("sayHi");
			cc.defrost();
			m.insertBefore("if (\"Coll\".equals($1)){ throw new IllegalArgumentException(); }");
			cc.toClass();
			cc.detach();
		} catch (NotFoundException | CannotCompileException e) {
			throw new IllegalStateException(e);
		}

    }
    
	public void starting() {
		if (isExecuted){
			return;
        }
		markAsIllegalArgumentException();
		isExecuted = true;
	}
	
	public void contextLoaded(ConfigurableApplicationContext arg0) {
		// do nothing...		
	}

	public void contextPrepared(ConfigurableApplicationContext arg0) {
		// do nothing...		
	}

	public void environmentPrepared(ConfigurableEnvironment arg0) {
		// do nothing...		
	}

	public void finished(ConfigurableApplicationContext arg0, Throwable arg1) {
		// do nothing...		
	}



}
