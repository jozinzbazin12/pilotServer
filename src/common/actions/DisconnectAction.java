package common.actions;

import common.server.DisconnectException;

public class DisconnectAction extends Action{

	private static final long serialVersionUID = 7291698350818979338L;

	@Override
	public void doAction() {
		throw new DisconnectException();
	}

}
