package ru.ancientempires.serializable;

import org.atteo.classindex.IndexSubclasses;

import java.io.DataInputStream;
import java.io.DataOutputStream;

@IndexSubclasses
public interface SerializableData {

	void toData(DataOutputStream output) throws Exception;

	SerializableData fromData(DataInputStream input, LoaderInfo info) throws Exception;

}
