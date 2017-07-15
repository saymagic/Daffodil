package tech.saymagic.daffodil.plugin.asm

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import tech.saymagic.daffodil.plugin.ClassUtils
import tech.saymagic.daffodil.plugin.Constants;

/**
 * Created by caoyanming on 2017/6/11.
 */

public class DaffodilMethodVisiter extends MethodVisitor {

    int argStartIndex = 0

    boolean ignore = true
    String className
    String superName
    String methodName
    int accessFlag
    String description

    DaffodilMethodVisiter(int i) {
        super(i)
    }

    DaffodilMethodVisiter(int i, MethodVisitor methodVisitor) {
        super(i, methodVisitor)
    }

    DaffodilMethodVisiter(int api, MethodVisitor mv, String className, String superName, String methodName,
                          int accessFlag, String description, String[] exceptions) {
        super(api, mv)
        this.className = className
        this.superName = superName
        this.methodName = methodName
        this.accessFlag = accessFlag
        this.description = description
    }

    @Override
    void visitCode() {
        println "DaffodilMethodVisiter visitCode " + className + " " + methodName + " ignore = " + ignore
        boolean isStatic = (accessFlag & Opcodes.ACC_STATIC) != 0
        Type[] argTypes = Type.getArgumentTypes(description)
        argStartIndex = isStatic ? 0 : 1
        if (null != argTypes) {
            for (Type type : argTypes) {
                argStartIndex += ClassUtils.lenOfType(type)
            }
        }
        super.visitCode()
        doMethodEnterVist()
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if (Constants.DAFFODIL_ANNOTATION_TYPE.equals(desc)) {
            ignore = false
        }
        return super.visitAnnotation(desc, visible)
    }

    void doMethodEnterVist() {
        if (ignore) {
            return
        }
        if (mv == null) {
            println "DaffodilMethodVisiter doMethodEnterVist " + className + " " + methodName + " , mv is null"
        }
        Type[] argTypes = Type.getArgumentTypes(description)
        int size = argTypes.length
        if (size >= 0) {
            putInteger(size)
            super.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
            super.visitVarInsn(Opcodes.ASTORE, argStartIndex)

            for (int i = 1; i <= size; ++i) {
                super.visitVarInsn(Opcodes.ALOAD, i)
            }
            for (int i = size - 1; i >= 0; --i) {
                ClassUtils.autoBox(mv, argTypes[i])
                super.visitVarInsn(Opcodes.ALOAD, argStartIndex)
                super.visitInsn(Opcodes.SWAP)
                putInteger(i)
                super.visitInsn(Opcodes.SWAP)
                super.visitInsn(Opcodes.AASTORE)
            }

            super.visitVarInsn(Opcodes.ALOAD, argStartIndex)
            super.visitMethodInsn(Opcodes.INVOKESTATIC, Constants.METHODREMEMBER_CLASS_TYPE,
                    Constants.METHODREMEMBER_METHOD_ENTER_NAME, Constants.METHODREMEMBER_METHOD_ENTER_DESC, false)
        }

    }

    @Override
    void visitIincInsn(int localIndex, int increment) {
        super.visitIincInsn(mapIndex(localIndex), increment)
    }

    @Override
    void visitVarInsn(int opcode, int localIndex) {
        super.visitVarInsn(opcode, mapIndex(localIndex))
    }

    @Override
    void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
        super.visitLocalVariable(name, desc, signature, start, end, mapIndex(index))
    }

    @Override
    void visitInsn(int opcode) {
        if (!ignore) {
            if (opcode >= Opcodes.IRETURN && opcode < Opcodes.RETURN) {
                Type retType = Type.getReturnType(description)
                if (retType.sort == Type.LONG || retType.sort == Type.DOUBLE) {
                    super.visitInsn(Opcodes.DUP2)
                } else {
                    super.visitInsn(Opcodes.DUP)
                }
                ClassUtils.autoBox(mv, Type.getReturnType(description))
                super.visitMethodInsn(Opcodes.INVOKESTATIC, Constants.METHODREMEMBER_CLASS_TYPE,
                        Constants.METHODREMEMBER_METHOD_EXIT_NAME, Constants.METHODREMEMBER_METHOD_EXIT_DESC, false)
            } else if (opcode == Opcodes.RETURN) {
                super.visitInsn(Opcodes.ACONST_NULL)
                super.visitMethodInsn(Opcodes.INVOKESTATIC, Constants.METHODREMEMBER_CLASS_TYPE,
                        Constants.METHODREMEMBER_METHOD_EXIT_NAME, Constants.METHODREMEMBER_METHOD_EXIT_DESC, false)
            }
        }
        super.visitInsn(opcode);
    }

    void putInteger(int value) {
        if (value <= Byte.MAX_VALUE) {
            super.visitIntInsn(Opcodes.BIPUSH, value)
        } else if (value <= Short.MAX_VALUE) {
            super.visitIntInsn(Opcodes.SIPUSH, value)
        } else {
            super.visitLdcInsn(value)
        }
    }

    private int mapIndex(int localIndex) {
        return localIndex >= argStartIndex ? localIndex + 1 : localIndex
    }
}
