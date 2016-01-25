package jdbc;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.Map;

public class MyArray implements Array {

	String[] stringvalues;
	int[] integerValues;
	boolean[] booleanValues;
	long[] longValues;
	double[] doubleValues;
	float[] floatValues;
	String type;

	public MyArray(String s, String d) {
		s.replaceAll("\\s+", "");
		String[] parts = s.split(",");
		type = d;
		switch (d) {
		case "int":
			integerValues = new int[parts.length];
			for (int i = 0; i < parts.length; i++)
				integerValues[i] = Integer.parseInt(parts[i]);
			break;
		case "float":
			floatValues = new float[parts.length];
			for (int i = 0; i < parts.length; i++)
				floatValues[i] = Float.parseFloat(parts[i]);
			break;
		case "long":
			longValues = new long[parts.length];
			for (int i = 0; i < parts.length; i++)
				longValues[i] = Long.parseLong(parts[i]);
			break;
		case "double":
			doubleValues = new double[parts.length];
			for (int i = 0; i < parts.length; i++)
				doubleValues[i] = Double.parseDouble(parts[i]);
			break;
		case "boolean":
			booleanValues = new boolean[parts.length];
			for (int i = 0; i < parts.length; i++)
				booleanValues[i] = Boolean.parseBoolean(parts[i]);
			break;
		default:
			break;
		}
	}

	@Override
	public void free() throws SQLException {

	}

	@Override
	public Object getArray() throws SQLException {
		switch (type) {
		case "int":
			Integer[] result = new Integer[integerValues.length];
			for (int i = 0; i < result.length; i++)
				result[i] = integerValues[i];
			return result;
		case "varchar":
			String[] result1 = new String[stringvalues.length];
			for (int i = 0; i < result1.length; i++)
				result1[i] = stringvalues[i];
			return result1;
		case "float":
			float[] result2 = new float[floatValues.length];
			for (int i = 0; i < result2.length; i++)
				result2[i] = floatValues[i];
			return result2;
		case "long":
			long[] result3 = new long[longValues.length];
			for (int i = 0; i < result3.length; i++)
				result3[i] = longValues[i];
			return result3;
		case "double":
			double[] result4 = new double[doubleValues.length];
			for (int i = 0; i < result4.length; i++)
				result4[i] = doubleValues[i];
			return result4;
		case "boolean":
			boolean[] result5 = new boolean[booleanValues.length];
			for (int i = 0; i < result5.length; i++)
				result5[i] = booleanValues[i];
			return result5;

		default:
			return null;
		}
	}

	@Override
	public Object getArray(Map<String, Class<?>> arg0) throws SQLException {
		throw new SQLFeatureNotSupportedException(
				"this method is not supported");
	}

	@Override
	public Object getArray(long arg0, int arg1) throws SQLException {
		throw new SQLFeatureNotSupportedException(
				"this method is not supported");
	}

	@Override
	public Object getArray(long arg0, int arg1, Map<String, Class<?>> arg2)
			throws SQLException {
		throw new SQLFeatureNotSupportedException(
				"this method is not supported");
	}

	@Override
	public int getBaseType() throws SQLException {
		switch (type) {
		case "varchar":
			return Types.VARCHAR;
		case "float":
			return Types.FLOAT;
		case "long":
			return Types.BIGINT;
		case "double":
			return Types.DOUBLE;
		case "boolean":
			return Types.BOOLEAN;
		default:
			return -1;
		}
	}

	@Override
	public String getBaseTypeName() throws SQLException {
		return type;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		throw new SQLFeatureNotSupportedException(
				"this method is not supported");
	}

	@Override
	public ResultSet getResultSet(Map<String, Class<?>> arg0)
			throws SQLException {
		throw new SQLFeatureNotSupportedException(
				"this method is not supported");
	}

	@Override
	public ResultSet getResultSet(long arg0, int arg1) throws SQLException {
		throw new SQLFeatureNotSupportedException(
				"this method is not supported");
	}

	@Override
	public ResultSet getResultSet(long arg0, int arg1,
			Map<String, Class<?>> arg2) throws SQLException {
		throw new SQLFeatureNotSupportedException(
				"this method is not supported");
	}
}
