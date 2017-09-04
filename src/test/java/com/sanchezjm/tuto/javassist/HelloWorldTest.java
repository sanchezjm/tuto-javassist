package com.sanchezjm.tuto.javassist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

public class HelloWorldTest {

	@BeforeClass
	public static void setUp() throws NotFoundException, CannotCompileException, IOException{
		final ClassPool pool = ClassPool.getDefault();
		final CtClass cc = pool.get("com.sanchezjm.tuto.javassist.HelloWorld");
		final CtMethod m = cc.getDeclaredMethod("sayHi");
		m.insertBefore("System.out.println(\" param: \" + $1); if (\"Coll\".equals($1)){ throw new IllegalArgumentException(); }");
		cc.toClass();
	}
	
	@Test
	public void mustSayHi(){
		HelloWorld helloWorld = new HelloWorld();	
		String text = helloWorld.sayHi("Tip");
		assertEquals("Hi Tip!", text);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void mustThrowsIllegalArgumentExceptionIfTheName(){
		HelloWorld helloWorld = new HelloWorld();
		helloWorld.sayHi("Coll");
		fail("must throw IllegalArgumentException");
	}
	
}
