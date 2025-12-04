package eater.utils;

@FunctionalInterface
public interface PropertyChangedListener<T> {
	void propertyChanged(T newValue, T oldValue);
}
