package asset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.io.File;
import java.io.InputStream;
import java.awt.Color;

/**	Class for an object containing
 *	properties of an object. DataMaps
 *	have support for file input and
 *	output. Explicitly supported data
 *	types are documented in {@link DataType}.
 *@author Britton
 */
public class DataMap extends HashMap<String,String>{
	private static final long serialVersionUID = -1176140801362107135L;
	private boolean safe = true;

	/**	Data types explicitly supported in
	 *	{@code DataMap}s.</br>Data Types:
	 *	<ul>
	 *		<li>{@link #STRING}:</br>
	 *				<i>Data type</i></br>
	 *			String value. Spaces are unsupported,
	 *			and the formatting for spaces is thus
	 *			the responsibility of the accessor of
	 *			the entry.</br>
	 *			Keyword: <i>{@code str}</i></br>
	 *			Class: {@link String}{@code .class}</br>
	 *			Syntax: <i>{@code str}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #INTEGER}:</br>
	 *				<i>Data type</i></br>
	 *			Integer value.</br>
	 *			Keyword: <i>{@code int}</i></br>
	 *			Class: {@link Integer}{@code .class}</br>
	 *			Syntax: <i>{@code int}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #BOOLEAN}:</br>
	 *				<i>Data type</i></br>
	 *			A boolean value.</br>
	 *			Keyword: <i>{@code bool}</i></br>
	 *			Class: {@link Boolean}{@code .class}</br>
	 *			Syntax: <i>{@code bool}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #FLOAT}:</br>
	 *				<i>Data type</i></br>
	 *			A floating point number.</br>
	 *			Keyword: <i>{@code float}</i></br>
	 *			Class: {@link Float}{@code .class}</br>
	 *			Syntax: <i>{@code float}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #DOUBLE}:</br>
	 *				<i>Data type</i></br>
	 *			A double value.</br>
	 *			Keyword: <i>{@code double}</i></br>
	 *			Class: {@link Double}{@code .class}</br>
	 *			Syntax: <i>[@code double}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #LONG}:</br>
	 *				<i>Data type</i></br>
	 *			A long value.</br>
	 *			Keyword: <i>{@code long}</i></br>
	 *			Class: {@link Long}{@code .class}</br>
	 *			Syntax: <i>{@code long}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #SHORT}:</br>
	 *				<i>Data type</i></br>
	 *			A short value.</br>
	 *			Keyword: <i>{@code short}</i></br>
	 *			Class: {@link Short}{@code .class}</br>
	 *			Syntax: <i>{@code short}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #BYTE}:</br>
	 *				<i>Data type</i></br>
	 *			A byte value.</br>
	 *			Keyword: <i>{@code byte}</i></br>
	 *			Class: {@link Byte}{@code .class}</br>
	 *			Syntax: <i>{@code byte}</i> name <b>value</b>
	 *		</li>
	 *		<li>{@link #FILE}:</br>
	 *				<i>Data type</i></br>
	 *			A file on the system.</br>
	 *			Keyword: <i>{@code file}</i></br>
	 *			Class: {@link File}{@code .class}</br>
	 *			Syntax: <i>{@code file}</i> name <b>assetpath</b>
	 *		</li>
	 *		<li>{@link #COLOR}:</br>
	 *				<i>Data type</i></br>
	 *			A color in RGB or RGBA format.</br>
	 *			Keyword: <i>{@code color}</i></br>
	 *			Class: {@link Color}{@code .class}</br>
	 *			Syntax: <i>{@code color}</i> name <b>r,g,b</b></br>
	 *			<b><i>OR</i></b> <i>{@code color}</i> name <b>r,g,b,a</b>
	 *		</li>
	 *		<li>{@link #DATAMAP}:</br>
	 *				<i>Data type</i></br>
	 *			Another DataMap in the given file.</br>
	 *			Keyword: <i>{@code dat}</i></br>
	 *			Class: {@link DataMap}{@code .class}</br>
	 *			Syntax: <i>{@code dat}</i> name <b>assetpath</b>
	 *		</li>
	 *		<li>{@link #TEXTURE}:</br>
	 *				<i>Data type</i></br>
	 *			A texture from the given file.</br>
	 *			Keyword: <i>{@code texture}</i></br>
	 *			Class: {@link Texture}{@code .class}</br>
	 *			Syntax: <i>{@code texture}</i> name <b>resourcepath</b>
	 *		</li>
	 *		<li>{@link #SOUND}:</br>
	 *				<i>Data type</i></br>
	 *			A sound from the given file.</br>
	 *			Keyword: <i>{@code sound}</i></br>
	 *			Class: {@link Sound}{@code .class}</br>
	 *			Syntax: <i>{@code sound}</i> name <b>resourcepath</b>
	 *		</li>
	 *		<li>{@link #REFERENCE}:</br>
	 *				<i>Keyword</i></br>
	 *			Used to direct any access of the entry
	 *			to another entry.</br>
	 *			Keyword: <i>{@code ref}</i></br>
	 *			Class: <i>N/A</i></br>
	 *			Syntax: <i>{@code ref}</i> name <b>target</b>
	 *		</li>
	 *		<li>{@link #INHERITANCE}:</br>
	 *				<i>Keyword</i></br>
	 *			Used to add entries of another DataMap,
	 *			or to add one entry from the DataMap
	 *			indicated by the given file.</br>
	 *			Keyword: <i>{@code inherit}</i></br>
	 *			Class: <i>N/A</i></br>
	 *			Syntax: <i>{@code inherit}</i> <b>assetpath</b></br>
	 *			<b><i>OR</i></b> <i>{@code inherit}</i> <b><i>entry</i></b> <b>assetpath</b>
	 *		</li>
	 *	</ul>
	 *@author Britton
	 */
	public enum DataType{
		STRING("str",String.class),
		INTEGER("int",Integer.class),
		BOOLEAN("bool",Boolean.class),
		FLOAT("float",Float.class),
		DOUBLE("double",Double.class),
		LONG("long",Long.class),
		SHORT("short",Short.class),
		BYTE("byte",Byte.class),
		FILE("file",File.class),
		COLOR("color",Color.class),
		DATAMAP("dat",DataMap.class),
		TEXTURE("texture",Texture.class),
		SOUND("sound",Sound.class),
		REFERENCE("ref",null),
		INHERITANCE("inherit",null)
		;
		private final String name;
		private final Class<?> clazz;
		DataType(String arg0,Class<?> arg1){
			name = arg0;
			clazz = arg1;
		}
		public Class<?> typeClass(){return clazz;}
		public String keyword(){return name;}
		public static DataType get(String arg){
			for(DataType x : values())
				if(x.keyword().equals(arg))
					return x;
			return null;
		}
		public static DataType get(Class<?> arg){
			for(DataType x : values())
				if(x.typeClass().equals(arg))
					return x;
			return null;
		}
	}

	public DataMap(){super();}
	public DataMap(File arg){
		super();
		Scanner fi = null;
		try{
			fi = new Scanner(arg);
		}catch(Exception e){return;}
		while(fi.hasNextLine())
			add(fi.nextLine());
		fi.close();
	}
	public DataMap(InputStream arg){
		super();
		Scanner in = new Scanner(arg);
		while(in.hasNextLine())
			add(in.nextLine());
		in.close();
	}

	public void add(String arg){
		String[] tmp = arg.split(" ");
		if(DataType.get(tmp[0])==DataType.INHERITANCE)
			if(tmp.length==2)
				try{addFrom(ResourceLoader.getFile(tmp[1]));}
				catch(Exception e){}
			else if(tmp.length==3){
				try{add(ResourceLoader.getDataMap(tmp[2]).exportEntry(tmp[1]));}
				catch(Exception e){}
				return;
			}
		if(tmp.length<3)
			return;
		if((!safe) || (!containsKey(tmp[1])))
			put(tmp[1],(tmp[0] + " " + tmp[2]));
	}
	public <T> void add(String arg0,T arg1){add(parse(arg0,arg1));}
	public <T> void add(WrappedData<T> arg){add(arg.entryName(),arg.content);}
	public <T> void add(String arg0,WrappedData<T> arg1){add(arg0,arg1.content);}

	public <T> void modify(String arg0,T arg1){put(arg0,parseAsValue(arg1));}
	public <T> void modify(String arg0,WrappedData<T> arg1){modify(arg0,arg1.content);}

	public void addAll(String[] arg){
		for(String x : arg)
			add(x);
	}
	public void addAll(DataMap arg){addAll(arg.export().split("\n"));}
	public void addFrom(File arg){
		Scanner tmp;
		try{tmp = new Scanner(arg);}
		catch(Exception e){return;}
		while(tmp.hasNextLine())
			try{add(tmp.nextLine());}
			catch(Exception e){}
		tmp.close();
	}

	public String export(){
		String output = "";
		for(String k : this.keySet())
			output += (exportEntry(k) + "\n");
		return output.trim();
	}
	public String exportEntry(String arg){
		String[] tmp = get(arg).split(" ");
		return (tmp[0] + " " + arg + " " + tmp[1]);
	}

	public String get(String arg){return super.get(arg);}
	public DataType typeOf(String arg){
		DataType output = absoluteTypeOf(arg);
		if(output==DataType.REFERENCE)
			return typeOf(get(arg).split(" ")[1]);
		if(output==DataType.INHERITANCE)
			return DataType.DATAMAP;
		return output;
	}
	public DataType absoluteTypeOf(String arg){return DataType.get(get(arg).split(" ")[0]);}
	public Class<?> classOf(String arg){return typeOf(arg).typeClass();}
	public String nameOf(Class<?> arg){return DataType.get(arg).keyword();}

	public boolean isSafe(){return safe;}
	public void setSafety(boolean arg){safe = arg;}

	/**
	 *@author Britton Wolfe
	 *@param <W> The type of the
	 *	value wrapped in this object
	 *	({@link #content}).
	 */
	public static class WrappedData<W>{
		public final W content;
		private final String ename;
		public WrappedData(W arg){this(arg,null);}
		public WrappedData(W arg0,String arg1){
			content = arg0;
			ename = arg1;
		}
		public String entryName(){return ename;}
		public String toString(){return content.toString();}
	}

	/**
	 *@param arg The name of the property
	 *	to return the value of.
	 *@return The entry of <b>arg</b>
	 *	contained within a {@code
	 *	WrappedData} object.
	 */
	public WrappedData<?> getWrappedData(String arg){
		Object tmp = getData(arg,classOf(arg));
		if(tmp instanceof String)
			return new WrappedData<String>((String)tmp,arg);
		if(tmp instanceof Integer)
			return new WrappedData<Integer>((Integer)tmp,arg);
		if(tmp instanceof Boolean)
			return new WrappedData<Boolean>((Boolean)tmp,arg);
		if(tmp instanceof Float)
			return new WrappedData<Float>((Float)tmp,arg);
		if(tmp instanceof Double)
			return new WrappedData<Double>((Double)tmp,arg);
		if(tmp instanceof Long)
			return new WrappedData<Long>((Long)tmp,arg);
		if(tmp instanceof Short)
			return new WrappedData<Short>((Short)tmp,arg);
		if(tmp instanceof Byte)
			return new WrappedData<Byte>((Byte)tmp,arg);
		if(tmp instanceof File)
			return new WrappedData<File>((File)tmp,arg);
		if(tmp instanceof Color)
			return new WrappedData<Color>((Color)tmp,arg);
		if(tmp instanceof DataMap)
			return new WrappedData<DataMap>((DataMap)tmp,arg);
		if(tmp instanceof Texture)
			return new WrappedData<Texture>((Texture)tmp,arg);
		if(tmp instanceof Sound)
			return new WrappedData<Sound>((Sound)tmp,arg);
		return null;
	}
	/**
	 *@param <T> The type that should be returned..
	 *@param arg The name of the entry to return
	 *	the value of.
	 *@return Probably the same thing as
	 *	{@code getData(<b>arg</b>,classOf(<b>arg</b>))}
	 *	(in principle).
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(String arg){return ((WrappedData<T>)getWrappedData(arg)).content;}
	/**	Returns the value of the given
	 *	property (<b>arg0</b>) as an
	 *	object of the given class
	 *	(<b>arg1</b>).
	 *@param arg0 The name of the
	 *	property to return the value
	 *	of.
	 *@param arg1 The class that the
	 *	returned value should be an
	 *	object of.
	 *@return The value, as an object
	 *	of the class <b>arg1</b>, of
	 *	<b>arg0</b>, or {@code null}
	 *	if the property <b>arg0</b>
	 *	is not present. In some cases,
	 *	{@code null} will be returned
	 *	if an exception occurs.
	 */
	public <T> T getData(String arg0,Class<T> arg1){
		String val = get(arg0);
		String ed = val.split(" ")[1];
		DataType dt = DataType.get(val.split(" ")[0]);
		if(dt==DataType.REFERENCE)
			return getData(ed,arg1);
		switch(DataType.get(arg1)){
		case INTEGER:
			return arg1.cast(Integer.parseInt(ed));
		case BOOLEAN:
			return arg1.cast(Boolean.parseBoolean(ed));
		case FLOAT:
			return arg1.cast(Float.parseFloat(ed));
		case DOUBLE:
			return arg1.cast(Double.parseDouble(ed));
		case LONG:
			return arg1.cast(Long.parseLong(ed));
		case SHORT:
			return arg1.cast(Short.parseShort(ed));
		case BYTE:
			return arg1.cast(Byte.parseByte(ed));
		case FILE:
			try{return arg1.cast(ResourceLoader.getFile(ed));}
			catch(Exception e1){return null;}
		case COLOR:
			String[] vs = ed.split(",");
			int r = Integer.parseInt(vs[0]),
				g = Integer.parseInt(vs[1]),
				b = Integer.parseInt(vs[2]),
				a = (vs.length==4) ? Integer.parseInt(vs[3]) : -1;
			return arg1.cast((a!=-1) ? (new Color(r,g,b,a)) : (new Color(r,g,b)));
		case DATAMAP:
			try{return arg1.cast(new DataMap(ResourceLoader.getStream(ed)));}
			catch(Exception e){return null;}
		case TEXTURE:
			try{return arg1.cast(ResourceLoader.getTexture(ResourceLoader.getTextureStream(ed)));}
			catch(Exception e){return null;}
		case SOUND:
			try{return arg1.cast(ResourceLoader.getSound(ed));}
			catch(Exception e){return null;}
		default:
			try{return arg1.cast(ed);}
			catch(Exception e){return null;}
		}
	}

	public <T> String parse(String arg0,T arg1){return (DataType.get(arg1.getClass()).keyword() + " " + arg0 + " " + arg1.toString());}
	public <T> String parseAsValue(T arg){return (DataType.get(arg.getClass()).keyword() + " " + arg.toString());}

	public String toString(){
		String output = "";
		String[] k = this.keySet().toArray(new String[this.size()]);
		for(String x : k)
			output += (x + ": " + get(x) + "\n");
		return output.trim();
	}

	public abstract class DataMapIterator<E> implements Iterator<WrappedData<E>>, Iterable<WrappedData<E>>{
		private int currentIndex;
		private final DataType dt;

		protected DataMapIterator(DataType arg){
			currentIndex = 0;
			dt = arg;
		}

		public boolean hasNext(){return (probeNext(false)!=null);}
		public WrappedData<E> next(){return probeNext(true);}
		public E getNext(){return next().content;}
		public WrappedData<E> probeNext(boolean arg){
			for(int x=getCurrentIndex(); x<DataMap.this.size(); x++){
				String entryName = DataMap.this.keySet().toArray(new String[DataMap.this.size()])[x];
				if(typeOf(entryName)==dataType()){
					if(arg)
						setCurrentIndex(x+1);
					return new WrappedData<E>(DataMap.this.<E>getData(entryName),entryName);
				}
			}
			return null;
		}

		protected int getCurrentIndex(){return currentIndex;}
		protected void setCurrentIndex(int arg){currentIndex = arg;}
		public DataType dataType(){return dt;}

		public void remove(){}
		public Iterator<WrappedData<E>> iterator(){return this;}

	}

	public class DataMapStringIterator extends DataMapIterator<String>{public DataMapStringIterator(){super(DataType.STRING);}}
	public class DataMapIntegerIterator extends DataMapIterator<Integer>{public DataMapIntegerIterator(){super(DataType.INTEGER);}}
	public class DataMapBooleanIterator extends DataMapIterator<Boolean>{public DataMapBooleanIterator(){super(DataType.BOOLEAN);}}
	public class DataMapFloatIterator extends DataMapIterator<Float>{public DataMapFloatIterator(){super(DataType.FLOAT);}}
	public class DataMapDoubleIterator extends DataMapIterator<Double>{public DataMapDoubleIterator(){super(DataType.DOUBLE);}}
	public class DataMapLongIterator extends DataMapIterator<Long>{public DataMapLongIterator(){super(DataType.LONG);}}
	public class DataMapShortIterator extends DataMapIterator<Short>{public DataMapShortIterator(){super(DataType.SHORT);}}
	public class DataMapByteIterator extends DataMapIterator<Byte>{public DataMapByteIterator(){super(DataType.BYTE);}}
	public class DataMapFileIterator extends DataMapIterator<File>{public DataMapFileIterator(){super(DataType.FILE);}}
	public class DataMapColorIterator extends DataMapIterator<Color>{public DataMapColorIterator(){super(DataType.COLOR);}}
	public class DataMapDataMapIterator extends DataMapIterator<DataMap>{public DataMapDataMapIterator(){super(DataType.DATAMAP);}}
	public class DataMapTextureIterator extends DataMapIterator<Texture>{public DataMapTextureIterator(){super(DataType.TEXTURE);}}
	public class DataMapSoundIterator extends DataMapIterator<Sound>{public DataMapSoundIterator(){super(DataType.SOUND);}}

	@SuppressWarnings("unchecked")
	public <E> DataMapIterator<E> iterator(Class<E> arg){
		switch(DataType.get(arg)){
		case STRING:
			return (DataMapIterator<E>)stringIterator();
		case INTEGER:
			return (DataMapIterator<E>)integerIterator();
		case BOOLEAN:
			return (DataMapIterator<E>)booleanIterator();
		case FLOAT:
			return (DataMapIterator<E>)floatIterator();
		case DOUBLE:
			return (DataMapIterator<E>)doubleIterator();
		case LONG:
			return (DataMapIterator<E>)longIterator();
		case SHORT:
			return (DataMapIterator<E>)shortIterator();
		case BYTE:
			return (DataMapIterator<E>)byteIterator();
		case FILE:
			return (DataMapIterator<E>)fileIterator();
		case COLOR:
			return (DataMapIterator<E>)colorIterator();
		case DATAMAP:
			return (DataMapIterator<E>)dataMapIterator();
		case TEXTURE:
			return (DataMapIterator<E>)textureIterator();
		case SOUND:
			return (DataMapIterator<E>)soundIterator();
		default:
			return null;
		}
	}
	public DataMapStringIterator stringIterator(){return new DataMapStringIterator();}
	public DataMapIntegerIterator integerIterator(){return new DataMapIntegerIterator();}
	public DataMapBooleanIterator booleanIterator(){return new DataMapBooleanIterator();}
	public DataMapFloatIterator floatIterator(){return new DataMapFloatIterator();}
	public DataMapDoubleIterator doubleIterator(){return new DataMapDoubleIterator();}
	public DataMapLongIterator longIterator(){return new DataMapLongIterator();}
	public DataMapShortIterator shortIterator(){return new DataMapShortIterator();}
	public DataMapByteIterator byteIterator(){return new DataMapByteIterator();}
	public DataMapFileIterator fileIterator(){return new DataMapFileIterator();}
	public DataMapColorIterator colorIterator(){return new DataMapColorIterator();}
	public DataMapDataMapIterator dataMapIterator(){return new DataMapDataMapIterator();}
	public DataMapTextureIterator textureIterator(){return new DataMapTextureIterator();}
	public DataMapSoundIterator soundIterator(){return new DataMapSoundIterator();}

}
