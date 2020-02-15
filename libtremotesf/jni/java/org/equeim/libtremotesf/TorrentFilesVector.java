/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.equeim.libtremotesf;

public class TorrentFilesVector extends java.util.AbstractList<TorrentFile> implements java.util.RandomAccess {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected TorrentFilesVector(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(TorrentFilesVector obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        libtremotesfJNI.delete_TorrentFilesVector(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public TorrentFilesVector(TorrentFile[] initialElements) {
    this();
    reserve(initialElements.length);

    for (TorrentFile element : initialElements) {
      add(element);
    }
  }

  public TorrentFilesVector(Iterable<TorrentFile> initialElements) {
    this();
    for (TorrentFile element : initialElements) {
      add(element);
    }
  }

  public TorrentFile get(int index) {
    return doGet(index);
  }

  public TorrentFile set(int index, TorrentFile e) {
    return doSet(index, e);
  }

  public boolean add(TorrentFile e) {
    modCount++;
    doAdd(e);
    return true;
  }

  public void add(int index, TorrentFile e) {
    modCount++;
    doAdd(index, e);
  }

  public TorrentFile remove(int index) {
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

  public TorrentFilesVector() {
    this(libtremotesfJNI.new_TorrentFilesVector__SWIG_0(), true);
  }

  public TorrentFilesVector(TorrentFilesVector other) {
    this(libtremotesfJNI.new_TorrentFilesVector__SWIG_1(TorrentFilesVector.getCPtr(other), other), true);
  }

  public long capacity() {
    return libtremotesfJNI.TorrentFilesVector_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    libtremotesfJNI.TorrentFilesVector_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return libtremotesfJNI.TorrentFilesVector_isEmpty(swigCPtr, this);
  }

  public void clear() {
    libtremotesfJNI.TorrentFilesVector_clear(swigCPtr, this);
  }

  public TorrentFilesVector(int count, TorrentFile value) {
    this(libtremotesfJNI.new_TorrentFilesVector__SWIG_2(count, TorrentFile.getCPtr(value), value), true);
  }

  private int doSize() {
    return libtremotesfJNI.TorrentFilesVector_doSize(swigCPtr, this);
  }

  private void doAdd(TorrentFile x) {
    libtremotesfJNI.TorrentFilesVector_doAdd__SWIG_0(swigCPtr, this, TorrentFile.getCPtr(x), x);
  }

  private void doAdd(int index, TorrentFile x) {
    libtremotesfJNI.TorrentFilesVector_doAdd__SWIG_1(swigCPtr, this, index, TorrentFile.getCPtr(x), x);
  }

  private TorrentFile doRemove(int index) {
    long cPtr = libtremotesfJNI.TorrentFilesVector_doRemove(swigCPtr, this, index);
    return (cPtr == 0) ? null : new TorrentFile(cPtr, false);
  }

  private TorrentFile doGet(int index) {
    long cPtr = libtremotesfJNI.TorrentFilesVector_doGet(swigCPtr, this, index);
    return (cPtr == 0) ? null : new TorrentFile(cPtr, true);
  }

  private TorrentFile doSet(int index, TorrentFile val) {
    long cPtr = libtremotesfJNI.TorrentFilesVector_doSet(swigCPtr, this, index, TorrentFile.getCPtr(val), val);
    return (cPtr == 0) ? null : new TorrentFile(cPtr, false);
  }

  private void doRemoveRange(int fromIndex, int toIndex) {
    libtremotesfJNI.TorrentFilesVector_doRemoveRange(swigCPtr, this, fromIndex, toIndex);
  }

}
