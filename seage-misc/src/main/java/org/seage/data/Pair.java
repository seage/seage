package org.seage.data;

public class Pair<T> {
  public T _first;
  public T _second;

  public Pair(T first, T second) {
    _first = first;
    _second = second;
  }

  public T getFirst() {
    return _first;
  }

  public T getSecond() {
    return _second;
  }
}
