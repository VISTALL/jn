package crypt.helpers;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date: 19:23 07/12/2009
 */
public class Obfuscator
{
	private boolean _isEnable;
	private int _seed;
	private int _firstSize;
	private int _secondSize;
	//private int _thirdSize;
	private int[] _decodeTable1;
	private int[] _decodeTable2;
	//private int[] _decodeTable3;

	public Obfuscator()
	{
		disable();
	}

	public void disable()
	{
		_isEnable = false;
		_seed = 0;
		_firstSize = (_secondSize = 0);
		_decodeTable1 = (_decodeTable2 = null);
	}

	public void init_tables(int seed)
	{
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;

		_firstSize = 0xd0;   //длина первых опкодов
		_secondSize = 0xBF;   //длина вторых опкодов
	//	_thirdSize = 0x06;   //длина треьих опкодов

		_decodeTable1 = new int[_firstSize + 1];
		_decodeTable2 = new int[_secondSize + 1];
	//	_decodeTable3 = new int[_thirdSize + 1];

		for (i = 0; i <= _firstSize; ++i)
		{
			_decodeTable1[i] = i;
		}

		for (i = 0; i <= _secondSize; ++i)
		{
			_decodeTable2[i] = i;
		}

	/*	for (i = 0; i <= _thirdSize; ++i)
		{
			_decodeTable3[i] = i;
		}   */

		pseudo_rand_seed(seed);

		for (i = 1; i <= _firstSize; ++i)
		{
			k = pseudo_rand() % (i + 1);
			j = _decodeTable1[k];
			_decodeTable1[k] = _decodeTable1[i];
			_decodeTable1[i] = j;
		}

		for (i = 1; i <= _secondSize; ++i)
		{
			k = pseudo_rand() % (i + 1);
			j = _decodeTable2[k];
			_decodeTable2[k] = _decodeTable2[i];
			_decodeTable2[i] = j;
		}

	/*	for (i = 1; i <= _thirdSize; ++i)
		{
			k = pseudo_rand() % (i + 1);
			j = _decodeTable3[k];
			_decodeTable3[k] = _decodeTable3[i];
			_decodeTable3[i] = j;
		}  */

		//replace(0x12);
		//replace(0xb1);

		replace((byte)0x11);

		_isEnable = true;
	}

	private void replace(byte val)
	{
		int i;
		int j;
		boolean flag = false;
		for (i = 0; i < _decodeTable1.length; i++)
		{
			if(_decodeTable1[i] == val)
			{
				flag = true;
				break;
			}
		}

		if(!flag)
		{
			System.out.println("not found " + Integer.toHexString(val));
			return;
		}

		j = _decodeTable1[val];
		_decodeTable1[val] = val;
		_decodeTable1[i] = j;
	}

	public int decodeSingleOpcode(int id)
	{
		if (!_isEnable)
		{
			return id;
		}
		if (id > _firstSize)
		{
			return id;
		}
		return _decodeTable1[id];
	}

	public int decodeDoubleOpcode(int id)
	{
		if (!_isEnable)
		{
			return id;
		}
		if (id > _secondSize)
		{
			return id;
		}
		return _decodeTable2[id];
	}

/*	public int decodeTripleOpcode(int id)
	{
		if (!_isEnable)
		{
			return id;
		}
		if (id > _thirdSize)
		{
			return id;
		}
		id = _decodeTable3[id];
		return id;
	} */

	private void pseudo_rand_seed(int id)
	{
		_seed = id;
	}

	private int pseudo_rand()
	{
		_seed = _seed * 214013 + 2531011;
		return _seed >> 16 & 0x7FFF;
	}
}

