package fr.badblock.game.core18R3.players.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

public class ReflectionUtil
{
  public static final String serverVersion = null;
  
  static
  {
    try
    {
      Class.forName("org.bukkit.Bukkit");
      setObject(ReflectionUtil.class, null, "serverVersion", Bukkit.getServer().getClass().getPackage().getName()
        .substring(Bukkit.getServer().getClass().getPackage().getName().lastIndexOf('.') + 1));
    }
    catch (Exception localException) {}
  }
  
  public static Field getField(Class<?> clazz, String fname)
    throws Exception
  {
    Field f = null;
    try
    {
      f = clazz.getDeclaredField(fname);
    }
    catch (Exception e)
    {
      f = clazz.getField(fname);
    }
    setFieldAccessible(f);
    return f;
  }
  
  public static void setFieldAccessible(Field f)
    throws Exception
  {
    f.setAccessible(true);
    Field modifiers = Field.class.getDeclaredField("modifiers");
    modifiers.setAccessible(true);
    modifiers.setInt(f, f.getModifiers() & 0xFFFFFFEF);
  }
  
  public static Object getObject(Object obj, String fname)
    throws Exception
  {
    return getField(obj.getClass(), fname).get(obj);
  }
  
  public static Object getObject(Class<?> clazz, Object obj, String fname)
    throws Exception
  {
    return getField(clazz, fname).get(obj);
  }
  
  public static void setObject(Object obj, String fname, Object value)
    throws Exception
  {
    getField(obj.getClass(), fname).set(obj, value);
  }
  
  public static void setObject(Class<?> clazz, Object obj, String fname, Object value)
    throws Exception
  {
    getField(clazz, fname).set(obj, value);
  }
  
  public static Method getMethod(Class<?> clazz, String mname)
    throws Exception
  {
    Method m = null;
    try
    {
      m = clazz.getDeclaredMethod(mname, new Class[0]);
    }
    catch (Exception e)
    {
      try
      {
        m = clazz.getMethod(mname, new Class[0]);
      }
      catch (Exception ex)
      {
        return m;
      }
    }
    m.setAccessible(true);
    return m;
  }
  
  public static Method getMethod(Class<?> clazz, String mname, Class<?>... args)
    throws Exception
  {
    Method m = null;
    try
    {
      m = clazz.getDeclaredMethod(mname, args);
    }
    catch (Exception e)
    {
      try
      {
        m = clazz.getMethod(mname, args);
      }
      catch (Exception ex)
      {
        return m;
      }
    }
    m.setAccessible(true);
    return m;
  }
  
  public static Constructor<?> getConstructor(Class<?> clazz, Class<?>... args)
    throws Exception
  {
    Constructor<?> c = clazz.getConstructor(args);
    c.setAccessible(true);
    return c;
  }
  
  public static Object getFirstObject(Class<?> clazz, Class<?> objclass, Object instance)
    throws Exception
  {
    Field f = null;
    for (Field fi : clazz.getDeclaredFields()) {
      if (fi.getType().equals(objclass))
      {
        f = fi;
        break;
      }
    }
    if (f == null) {
      for (Field fi : clazz.getFields()) {
        if (fi.getType().equals(objclass))
        {
          f = fi;
          break;
        }
      }
    }
    setFieldAccessible(f);
    return f.get(instance);
  }
  
  public static Enum<?> getEnum(Class<?> clazz, String enumname, String constant)
    throws Exception
  {
    Class<?> c = Class.forName(clazz.getName() + "$" + enumname);
    @SuppressWarnings("rawtypes")
	Enum[] econstants = (Enum[])c.getEnumConstants();
    for (Enum<?> e : econstants) {
      if (e.name().equalsIgnoreCase(constant)) {
        return e;
      }
    }
    throw new Exception("Enum constant not found " + constant);
  }
  
  public static Enum<?> getEnum(Class<?> clazz, String constant)
    throws Exception
  {
    Class<?> c = Class.forName(clazz.getName());
    @SuppressWarnings("rawtypes")
	Enum[] econstants = (Enum[])c.getEnumConstants();
    for (Enum<?> e : econstants) {
      if (e.name().equalsIgnoreCase(constant)) {
        return e;
      }
    }
    throw new Exception("Enum constant not found " + constant);
  }
  
  public static Class<?> getNMSClass(String clazz)
    throws Exception
  {
    return Class.forName("net.minecraft.server." + serverVersion + "." + clazz);
  }
  
  public static Class<?> getBukkitClass(String clazz)
    throws Exception
  {
    return Class.forName("org.bukkit.craftbukkit." + serverVersion + "." + clazz);
  }
  
  public static Object invokeMethod(Class<?> clazz, Object obj, String method, Class<?>[] args, Object... initargs)
    throws Exception
  {
    return getMethod(clazz, method, args).invoke(obj, initargs);
  }
  
  public static Object invokeMethod(Class<?> clazz, Object obj, String method)
    throws Exception
  {
    return getMethod(clazz, method).invoke(obj, new Object[0]);
  }
  
  public static Object invokeMethod(Class<?> clazz, Object obj, String method, Object... initargs)
    throws Exception
  {
    return getMethod(clazz, method).invoke(obj, initargs);
  }
  
  public static Object invokeMethod(Object obj, String method)
    throws Exception
  {
    return getMethod(obj.getClass(), method).invoke(obj, new Object[0]);
  }
  
  public static Object invokeMethod(Object obj, String method, Object[] initargs)
    throws Exception
  {
    return getMethod(obj.getClass(), method).invoke(obj, initargs);
  }
  
  public static Object invokeConstructor(Class<?> clazz, Class<?>[] args, Object... initargs)
    throws Exception
  {
    return getConstructor(clazz, args).newInstance(initargs);
  }
}
