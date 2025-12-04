package eater.cpu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CpuStatePublisher {
	private CpuStatePublisher() {}

	// Using a list of weak references to allow listeners to be garbage collected
	private static final List<WeakReference<CpuStateListener>> listeners = new ArrayList<>();

	public static void addListener(CpuStateListener listener) {
		listeners.add(new WeakReference<>(listener));
	}

	public static void removeListener(CpuStateListener listener) {
		listeners.removeIf(ref -> ref.get() == listener);
	}

	public static void notifyListeners(CpuState state) {
		for (WeakReference<CpuStateListener> ref : listeners) {
			CpuStateListener listener = ref.get();
			if (listener != null) {
				listener.onCpuStateChanged(state);
			} else {
				// Clean up the weak reference if the listener has been garbage collected
				listeners.remove(ref);
			}
		}
	}
}
