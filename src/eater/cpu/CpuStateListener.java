package eater.cpu;

@FunctionalInterface
public interface CpuStateListener {
	void onCpuStateChanged(CpuState state);
}
