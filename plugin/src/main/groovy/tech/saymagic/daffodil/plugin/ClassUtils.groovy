package tech.saymagic.daffodil.plugin

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

/**
 *
 * @author jianglee
 * @since 16/2/2017
 */
public final class ClassUtils {

    private static Map<Integer, Integer> storeOps = new HashMap<Integer, Integer>()
    private static Map<Integer, Integer> loadOps = new HashMap<Integer, Integer>()
    static {
        storeOps.put(Opcodes.ISTORE, 1)
        storeOps.put(Opcodes.LSTORE, 2)
        storeOps.put(Opcodes.FSTORE, 1)
        storeOps.put(Opcodes.DSTORE, 2)
        storeOps.put(Opcodes.ASTORE, 1)

        loadOps.put(Opcodes.ILOAD, 1)
        loadOps.put(Opcodes.LLOAD, 2)
        loadOps.put(Opcodes.FLOAD, 1)
        loadOps.put(Opcodes.DLOAD, 2)
        loadOps.put(Opcodes.ALOAD, 1)
    }

    public static int lenOfType(Type type) {
        if (type.equals(Type.DOUBLE_TYPE)
                || type.equals(Type.LONG_TYPE)) {
            return 2
        }
        return 1
    }

    public static int lenOfOpcode(int opcode) {
        if (isStoreOp(opcode)) {
            return storeOps.get(opcode)
        } else if (isLoadOp(opcode)) {
            return loadOps.get(opcode)
        } else {
            throw new RuntimeException("lenOfOpcode is unsupported for opcode(${opcode})")
        }
    }

    public static boolean isStoreOp(int opcode) {
        return storeOps.containsKey(opcode)
    }

    public static boolean isLoadOp(int opcode) {
        return loadOps.containsKey(opcode)
    }

    public static void checkCast(MethodVisitor mv, Type requireType) {
        String className = getBoxOrUnboxClassName(requireType)
        if (null != className) {
            // auto unBox
            mv.visitTypeInsn(Opcodes.CHECKCAST, className)
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, getUnboxMethodName(requireType), getUnboxMethodDesc(requireType), false);
        } else {
            // check cast
            if (requireType.sort == Type.ARRAY) {
                mv.visitTypeInsn(Opcodes.CHECKCAST, requireType.descriptor)
            } else {
                mv.visitTypeInsn(Opcodes.CHECKCAST, requireType.className.replaceAll("\\.", "/"))
            }
        }
    }

    public static void autoUnbox(MethodVisitor mv, Type requireType) {
        String className = getBoxOrUnboxClassName(requireType)
        if (null != className) {
            // auto box
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, className, "valueOf", getBoxMethodDesc(requireType), false);
        }
    }

    public static void autoBox(MethodVisitor mv, Type requireType) {
        String className = getBoxOrUnboxClassName(requireType)
        if (null != className) {
            // auto box
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, className, "valueOf", getBoxMethodDesc(requireType), false);
        }
    }

    public static String getBoxOrUnboxClassName(Type type) {
        String className = null
        switch (type.getSort()) {
            case Type.BOOLEAN:
                className = "java/lang/Boolean"
                break
            case Type.BYTE:
                className = "java/lang/Byte"
                break
            case Type.CHAR:
                className = "java/lang/Character"
                break
            case Type.SHORT:
                className = "java/lang/Short"
                break
            case Type.INT:
                className = "java/lang/Integer"
                break
            case Type.FLOAT:
                className = "java/lang/Float"
                break
            case Type.DOUBLE:
                className = "java/lang/Double"
                break
            case Type.LONG:
                className = "java/lang/Long"
                break
            case Type.OBJECT:
            case Type.ARRAY:
            case Type.VOID:
            case Type.METHOD:
                break
            default:
                println "unknown type : " + type.className
                break
        }
        return className
    }

    public static String getBoxMethodDesc(Type type) {
        String desc = null
        switch (type.getSort()) {
            case Type.BOOLEAN:
                desc = "(Z)Ljava/lang/Boolean;"
                break
            case Type.BYTE:
                desc = "(B)Ljava/lang/Byte;"
                break
            case Type.CHAR:
                desc = "(C)Ljava/lang/Character;"
                break
            case Type.SHORT:
                desc = "(S)Ljava/lang/Short;"
                break
            case Type.INT:
                desc = "(I)Ljava/lang/Integer;"
                break
            case Type.FLOAT:
                desc = "(F)Ljava/lang/Float;"
                break
            case Type.DOUBLE:
                desc = "(D)Ljava/lang/Double;"
                break
            case Type.LONG:
                desc = "(J)Ljava/lang/Long;"
                break
            case Type.OBJECT:
            case Type.ARRAY:
            case Type.VOID:
            case Type.METHOD:
                break
            default:
                println "unknown type : " + type.className
                break
        }
        return desc
    }

    public static String getUnboxMethodName(Type type) {
        String desc = null
        switch (type.getSort()) {
            case Type.BOOLEAN:
                desc = "booleanValue"
                break
            case Type.BYTE:
                desc = "byteValue"
                break
            case Type.CHAR:
                desc = "charValue"
                break
            case Type.SHORT:
                desc = "shortValue"
                break
            case Type.INT:
                desc = "intValue"
                break
            case Type.FLOAT:
                desc = "floatValue"
                break
            case Type.DOUBLE:
                desc = "doubleValue"
                break
            case Type.LONG:
                desc = "longValue"
                break
            case Type.OBJECT:
            case Type.ARRAY:
            case Type.VOID:
            case Type.METHOD:
                break
            default:
                throw new RuntimeException("unknown type:" + type.className)
        }
        return desc
    }

    public static String getUnboxMethodDesc(Type type) {
        String desc = null
        switch (type.getSort()) {
            case Type.BOOLEAN:
                desc = "()Z"
                break
            case Type.BYTE:
                desc = "()B"
                break
            case Type.CHAR:
                desc = "()C"
                break
            case Type.SHORT:
                desc = "()S"
                break
            case Type.INT:
                desc = "()I"
                break
            case Type.FLOAT:
                desc = "()F"
                break
            case Type.DOUBLE:
                desc = "()D"
                break
            case Type.LONG:
                desc = "()J"
                break
            case Type.OBJECT:
            case Type.ARRAY:
            case Type.VOID:
            case Type.METHOD:
                break
            default:
                throw new RuntimeException("unknown type:" + type.className)
        }
        return desc
    }
}