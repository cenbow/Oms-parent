package com.work.shop.oms.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RedisUtil
{
  public static final <T extends Serializable> List<T> decode(List<byte[]> byteList)
  {
    List objectList = new ArrayList(byteList.size());
    for (byte[] bytes : byteList) {
      if (null != bytes) {
        Serializable object = decode(bytes);
        objectList.add(object);
      }
      else {
        objectList.add(null);
      }
    }
    return objectList;
  }

  public static final <T extends Serializable> byte[] encode(T obj)
  {
    byte[] bytes = null;
    try {
      ByteArrayOutputStream bout = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(bout);
      out.writeObject(obj);
      bytes = bout.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Error serializing object" + obj + " => " + e);
    }

    return bytes;
  }

  public static final <T extends Serializable> T decode(byte[] bytes)
  {
    if (bytes == null) {
      return null;
    }
    Serializable t = null;
    Exception thrown = null;
    try {
      ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(bytes));

      t = (Serializable)oin.readObject();

      if (null != thrown)
        throw new RuntimeException("Error decoding byte[] data to instantiate java object - data at key may not have been of this type or even an object", thrown);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      thrown = e;

      if (null != thrown)
        throw new RuntimeException("Error decoding byte[] data to instantiate java object - data at key may not have been of this type or even an object", thrown);
    }
    catch (ClassNotFoundException e)
    {
      e.printStackTrace();
      thrown = e;

      if (null != thrown)
        throw new RuntimeException("Error decoding byte[] data to instantiate java object - data at key may not have been of this type or even an object", thrown);
    }
    catch (ClassCastException e)
    {
      e.printStackTrace();
      thrown = e;

      if (null != thrown)
        throw new RuntimeException("Error decoding byte[] data to instantiate java object - data at key may not have been of this type or even an object", thrown);
    }
    finally
    {
      if (null != thrown) {
        throw new RuntimeException("Error decoding byte[] data to instantiate java object - data at key may not have been of this type or even an object", thrown);
      }

    }

    return (T) t;
  }
}