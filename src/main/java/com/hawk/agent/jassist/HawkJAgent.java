package com.hawk.agent.jassist;

import com.hawk.agent.jassist.util.StringUtils;
import javassist.*;
import org.apache.bcel.Repository;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class HawkJAgent {
    public static void premain(String args, Instrumentation instrumentation) {
//        LOGGER.info("ClassPath: " + System.getProperty("java.class.path"));
//        LOGGER.info("ClassPath Ext: " + System.getProperty("java.ext.dirs"));
        instrumentation.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) throws IllegalClassFormatException {

                if ("javax/servlet/http/HttpServlet".equals(className)) {
                    System.out.println(className);
                    System.out.println("ClassLoader " + loader.getClass().getName());
                    try {
                        ClassPool cp = ClassPool.getDefault();
//                        cp.insertClassPath(new ClassClassPath(this.getClass()));
//                        cp.insertClassPath(new ClassClassPath(this.getClass()));
                        cp.insertClassPath(new LoaderClassPath(loader));

//                        CtClass.debugDump = "dump";
//                        System.out.println("Test javassist");
                        String clazzName = StringUtils.toDotClass(className);
                        CtClass ctClass = cp.getCtClass(clazzName);
//                        CtClass ctClass = cp.makeClass(new ByteArrayInputStream(classfileBuffer));
                        CtClass[] params = new CtClass[] {
                                cp.getCtClass("javax.servlet.http.HttpServletRequest"),
                                cp.getCtClass("javax.servlet.http.HttpServletResponse")
                        };

                        CtClass monitor = cp.getCtClass("com.hawk.agent.jassist.ServletMonitor");
                        monitor.toClass(loader, protectionDomain);
                        CtMethod ctMethod = ctClass.getDeclaredMethod("service", params);

                        ctMethod.insertBefore("com.hawk.agent.jassist.ServletMonitor.begin($$);");
                        ctMethod.insertAfter("com.hawk.agent.jassist.ServletMonitor.end($$);");
//                        ctMethod.insertBefore("System.out.println(\"Takes \" + (System.currentTimeMillis() - start) + \" ms\");");
//                        ctClass.writeFile("/home/ryan/Documents");
//                        LOGGER.info("Done....");
                        return ctClass.toBytecode();
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }
                } else if ("com/airparking/azkaban/TransferOrderDaily".equals(className)) {
//                    JavaClass jc = Repository.lookupClass(TestBean.class);
//                    ClassGen classGen = new ClassGen(jc);
//                    ConstantPoolGen cPoolGen = classGen.getConstantPool();
//
//                    Method method = classGen.getMethodAt(1);
//                    MethodGen methodGen = new MethodGen(method, jc.getClassName(), cPoolGen);
//                    InstructionList instructionHandles = methodGen.getInstructionList();
//
//                    InstructionHandle start = instructionHandles.getStart();
//                    instructionHandles.delete(start.getNext());
////            InstructionHandle end = instructionHandles.getEnd();
////            instructionHandles.delete(start, end);
//
//                    int index = cPoolGen.addUtf8("This is bcel test");
////            InstructionHandle getStatic = instructionHandles.getStart();
//                    InstructionHandle ldc = instructionHandles.append(start, new LDC(index));
////            InstructionHandle iconst0 = instructionHandles.append(start, new ICONST(0));
////            InstructionHandle anewarray = instructionHandles.append(iconst0, new ANEWARRAY(4));
////            InstructionHandle invokeVirtual = instructionHandles.append(anewarray, new INVOKEVIRTUAL(5));
////            InstructionHandle pop = instructionHandles.append(invokeVirtual, new POP());
////            InstructionHandle ret = instructionHandles.append(pop, new RETURN());
//
//                    classGen.replaceMethod(method, methodGen.getMethod());
//
//                    JavaClass newClass = classGen.getJavaClass();
////            newClass.
//                    newClass.dump("test/target/classes/com/spi/TestBean.class");
//
//                    TestBean testBean = new TestBean();
//                    testBean.begin();

                    ClassPool cp = ClassPool.getDefault();
                    cp.insertClassPath(new ClassClassPath(this.getClass()));
//                        cp.insertClassPath(new ClassClassPath(this.getClass()));
//                        cp.appendClassPath(new LoaderClassPath(loader));

//                        CtClass.debugDump = "dump";
//                        System.out.println("Test javassist");
                    String clazzName = StringUtils.toDotClass(className);
                    try {
                        CtClass ctClass = cp.getCtClass(clazzName);
                        CtMethod ctMethod = ctClass.getDeclaredMethod("testJavassist");
                        ctMethod.insertAfter("System.out.println(System.currentTimeMillis());");
                        return ctClass.toBytecode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return classfileBuffer;
            }
        });
    }
}
