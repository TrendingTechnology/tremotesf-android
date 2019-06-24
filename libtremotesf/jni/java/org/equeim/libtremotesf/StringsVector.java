/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.0
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class StringsVector extends java.util.AbstractList<String> implements java.util.RandomAccess {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected StringsVector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(StringsVector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtremotesfJNI.delete_StringsVector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public StringsVector(String[] initialElements) {
    this();
    for (String element : initialElements) {
      add(element);
    }
  }

  public StringsVector(Iterable<String> initialElements) {
    this();
    for (String element : initialElements) {
      add(element);
    }
  }

  public String get(int index) {
    return doGet(index);
  }

  public String set(int index, String e) {
    return doSet(index, e);
  }

  public boolean add(String e) {
    modCount++;
    doAdd(e);
    return true;
  }

  public void add(int index, String e) {
    modCount++;
    doAdd(index, e);
  }

  public String remove(int index) {
    modCount++;
    return doRemove(index);
  }

  protected void removeRange(int fromIndex, int toIndex) {
    modCount++;
    doRemoveRange(fromIndex, toIndex);
  }

  public int size() {
    return doSize();
  }

  public StringsVector() {
    this(libtremotesfJNI.new_StringsVector__SWIG_0(), true);
  }

  public StringsVector(StringsVector other) {
    this(libtremotesfJNI.new_StringsVector__SWIG_1(StringsVector.getCPtr(other), other), true);
  }

  public long capacity() {
    return libtremotesfJNI.StringsVector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtremotesfJNI.StringsVector_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return libtremotesfJNI.StringsVector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtremotesfJNI.StringsVector_clear(swigCPtr, this);
  }

  public StringsVector(int count, String value) {
    this(libtremotesfJNI.new_StringsVector__SWIG_2(count, value), true);
  }

  private int doSize() {
    return libtremotesfJNI.StringsVector_doSize(swigCPtr, this);
  }

  private void doAdd(String x) {
    libtremotesfJNI.StringsVector_doAdd__SWIG_0(swigCPtr, this, x);
  }

  private void doAdd(int index, String x) {
    libtremotesfJNI.StringsVector_doAdd__SWIG_1(swigCPtr, this, index, x);
  }

  private String doRemove(int index) {
    return libtremotesfJNI.StringsVector_doRemove(swigCPtr, this, index);
}

  private String doGet(int index) {
    return libtremotesfJNI.StringsVector_doGet(swigCPtr, this, index);
}

  private String doSet(int index, String val) {
    return libtremotesfJNI.StringsVector_doSet(swigCPtr, this, index, val);
}

  private void doRemoveRange(int fromIndex, int toIndex) {
    libtremotesfJNI.StringsVector_doRemoveRange(swigCPtr, this, fromIndex, toIndex);
  }

}
