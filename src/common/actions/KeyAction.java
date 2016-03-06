package common.actions;

public abstract class KeyAction extends Action{

	private static final long serialVersionUID = 3997511449278438648L;
	protected char c;

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		str.append(", ").append(c);
		return str.toString();
	}

	public void setC(char c) {
		this.c = c;
	}
	
}
