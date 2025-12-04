package utils;

@FunctionalInterface
public interface PropertyChangedListener<T> {
	void propertyChanged(T newValue, T oldValue);
}
