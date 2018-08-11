package org.seage.thread;

import org.seage.data.DataNode;

public abstract class Task implements Runnable
{
	private DataNode _result;
	private String _name;

    public Task(String name) {
    	this._name = name;
    }

    public DataNode getResult() {
        return _result;
    }

	public String getName() {
		return this._name;
	}
}