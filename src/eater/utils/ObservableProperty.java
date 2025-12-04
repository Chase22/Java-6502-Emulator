package eater.utils;

import java.util.LinkedList;
import java.util.List;

public class ObservableProperty<T> {
	private T value;

	private final List<PropertyChangedListener<T>> listeners = new LinkedList<>();

	public ObservableProperty(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		T oldValue = this.value;
		this.value = value;
		listeners.forEach(listener -> listener.propertyChanged(value, oldValue));
	}

	public void registerListener(PropertyChangedListener<T> listener) {
		listeners.add(listener);
	}

	public void unregisterListener(PropertyChangedListener<T> listener) {
		listeners.remove(listener);
	}
}
