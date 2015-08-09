package com.jds.jn.parser.formattree;

import com.jds.jn.parser.parttypes.PartType;

/**
 * @author VISTALL
 * @since 09.08.2015
 */
public class IfPart extends PartContainer
{
	private String myFieldName;
	private Operator myOperator;
	private int myValue;

	public enum Operator
	{
		GT
				{
					@Override
					public boolean test(int val1, int val2)
					{
						return val1 > val2;
					}
				},
		GT_EQ
				{
					@Override
					public boolean test(int val1, int val2)
					{
						return val1 >= val2;
					}
				},
		EQ
				{
					@Override
					public boolean test(int val1, int val2)
					{
						return val1 == val2;
					}
				},
		LT_EQ
				{
					@Override
					public boolean test(int val1, int val2)
					{
						return val1 <= val2;
					}
				},
		LT
				{
					@Override
					public boolean test(int val1, int val2)
					{
						return val1 < val2;
					}
				},
		NE
				{
					@Override
					public boolean test(int val1, int val2)
					{
						return val1 != val2;
					}
				};

		public abstract boolean test(int val1, int val2);
	}

	public IfPart(String fieldName, Operator operator, int value)
	{
		super(PartType.ifBlock, false);
		myFieldName = fieldName;
		myOperator = operator;
		myValue = value;
	}

	public String getFieldName()
	{
		return myFieldName;
	}

	public Operator getOperator()
	{
		return myOperator;
	}

	public int getValue()
	{
		return myValue;
	}
}
