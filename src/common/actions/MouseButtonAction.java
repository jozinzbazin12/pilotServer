package common.actions;

public abstract class MouseButtonAction extends Action {

	private static final long serialVersionUID = 5858471459649534833L;
	protected int key;

	public void setKey(int key) {
		this.key = key;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		str.append(", ").append(key);
		return str.toString();
	}

}
