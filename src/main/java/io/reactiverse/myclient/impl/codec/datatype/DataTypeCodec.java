package io.reactiverse.myclient.impl.codec.datatype;

import io.netty.buffer.ByteBuf;
import io.reactiverse.myclient.impl.util.BufferUtils;

import java.nio.charset.StandardCharsets;

//TODO charset injection
public class DataTypeCodec {
  // Sentinel used when an object is refused by the data type
  public static final Object REFUSED_SENTINEL = new Object();

  public static Object decodeText(DataType dataType, ByteBuf buffer) {
    switch (dataType) {
      //TODO just a basic implementation, can be optimised here
      case INT2:
        return textDecodeInt2(buffer);
      case INT3:
        return textDecodeInt3(buffer);
      case INT4:
        return textDecodeInt4(buffer);
      case INT8:
        return textDecodeInt8(buffer);
      case FLOAT:
        return textDecodeFloat(buffer);
      case DOUBLE:
        return textDecodeDouble(buffer);
      case VARCHAR:
        return textDecodeVarChar(buffer);
      default:
        return textDecodeVarChar(buffer);
    }
  }

  //TODO take care of unsigned numeric values here?
  public static void encodeBinary(DataType dataType, Object value, ByteBuf buffer) {
    // https://dev.mysql.com/doc/internals/en/binary-protocol-value.html
    switch (dataType) {
      case INT2:
        binaryEncodeInt2((Number) value, buffer);
        break;
      case INT3:
        binaryEncodeInt3((Number) value, buffer);
        break;
      case INT4:
        binaryEncodeInt4((Number) value, buffer);
        break;
      case INT8:
        binaryEncodeInt8((Number) value, buffer);
        break;
      case FLOAT:
        binaryEncodeFloat((Number) value, buffer);
        break;
      case DOUBLE:
        binaryEncodeDouble((Number) value, buffer);
        break;
      case VARCHAR:
        binaryEncodeVarChar(String.valueOf(value), buffer);
        break;
      default:
        binaryEncodeVarChar(String.valueOf(value), buffer);
        break;
    }
  }

  public static Object decodeBinary(DataType dataType, ByteBuf buffer) {
    switch (dataType) {
      case INT2:
        return binaryDecodeInt2(buffer);
      case INT3:
        return binaryDecodeInt3(buffer);
      case INT4:
        return binaryDecodeInt4(buffer);
      case INT8:
        return binaryDecodeInt8(buffer);
      case FLOAT:
        return binaryDecodeFloat(buffer);
      case DOUBLE:
        return binaryDecodeDouble(buffer);
      case VARCHAR:
        return binaryDecodeVarChar(buffer);
      default:
        return binaryDecodeVarChar(buffer);
    }
  }

  public static Object prepare(DataType type, Object value) {
    switch (type) {
      //TODO handle json + unknown?
      default:
        Class<?> javaType = type.decodingType;
        return value == null || javaType.isInstance(value) ? value : REFUSED_SENTINEL;
    }
  }

  private static void binaryEncodeInt2(Number value, ByteBuf buffer) {
    buffer.writeShortLE(value.intValue());
  }

  private static void binaryEncodeInt3(Number value, ByteBuf buffer) {
    buffer.writeMediumLE(value.intValue());
  }

  private static void binaryEncodeInt4(Number value, ByteBuf buffer) {
    buffer.writeIntLE(value.intValue());
  }

  private static void binaryEncodeInt8(Number value, ByteBuf buffer) {
    buffer.writeLongLE(value.longValue());
  }

  private static void binaryEncodeFloat(Number value, ByteBuf buffer) {
    buffer.writeFloatLE(value.floatValue());
  }

  private static void binaryEncodeDouble(Number value, ByteBuf buffer) {
    buffer.writeDoubleLE(value.doubleValue());
  }

  private static void binaryEncodeVarChar(String value, ByteBuf buffer) {
    BufferUtils.writeLengthEncodedString(buffer, value, StandardCharsets.UTF_8);
  }

  private static Short binaryDecodeInt2(ByteBuf buffer) {
    return buffer.readShortLE();
  }

  private static Integer binaryDecodeInt3(ByteBuf buffer) {
    return buffer.readIntLE();
  }

  private static Integer binaryDecodeInt4(ByteBuf buffer) {
    return buffer.readIntLE();
  }

  private static Long binaryDecodeInt8(ByteBuf buffer) {
    return buffer.readLongLE();
  }

  private static Float binaryDecodeFloat(ByteBuf buffer) {
    return buffer.readFloatLE();
  }

  private static Double binaryDecodeDouble(ByteBuf buffer) {
    return buffer.readDoubleLE();
  }

  private static String binaryDecodeVarChar(ByteBuf buffer) {
    return BufferUtils.readLengthEncodedString(buffer, StandardCharsets.UTF_8);
  }

  private static Short textDecodeInt2(ByteBuf buffer) {
    return Short.parseShort(buffer.toString(StandardCharsets.UTF_8));
  }

  private static Integer textDecodeInt3(ByteBuf buffer) {
    return Integer.parseInt(buffer.toString(StandardCharsets.UTF_8));
  }

  private static Integer textDecodeInt4(ByteBuf buffer) {
    return Integer.parseInt(buffer.toString(StandardCharsets.UTF_8));
  }

  private static Long textDecodeInt8(ByteBuf buffer) {
    return Long.parseLong(buffer.toString(StandardCharsets.UTF_8));
  }

  private static Float textDecodeFloat(ByteBuf buffer) {
    return Float.parseFloat(buffer.toString(StandardCharsets.UTF_8));
  }

  private static Double textDecodeDouble(ByteBuf buffer) {
    return Double.parseDouble(buffer.toString(StandardCharsets.UTF_8));
  }

  private static String textDecodeVarChar(ByteBuf buffer) {
    return buffer.toString(StandardCharsets.UTF_8);
  }
}