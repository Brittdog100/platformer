package asset;

public interface Propertied{

	/**
	 *@param arg0 The name of the property
	 *	to return the value of.
	 *@param arg1 The class of the data
	 *	type of the requested property.
	 *@return The value, as an object
	 *	of the class <b>arg1</b>, of
	 *	<b>arg0</b>, or {@code null}
	 *	if the property <b>arg0</b>
	 *	doesn't exist.
	 *@see {@link DataMap#getData(String,Class)}
	 */
	public <T> T getProperty(String arg0,Class<T> arg1);
	/**
	 *@param arg The name of the property
	 *	to return the value of.
	 *@return
	 */
	public DataMap.WrappedData<?> getProperty(String arg);
	public boolean hasProperty(String arg);
	public String propertiesToString();

	public void inject(String arg);
	public <T> void inject(String arg0,T arg1);
	public <T> void modify(String arg0,T arg1);

}
