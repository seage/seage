package org.seage.problem.sat;

import java.util.*;
import java.io.*;
/**
 * Summary description for InstanceReader.
 */
public class FormulaReader
{

    private String _path;
//    public int _countLiterals;

    public FormulaReader(String path){
        _path = path;
//        _countLiterals = loadNumberLiterals();
    }

	public Clause[] readClauses() throws IOException
	{
		LineNumberReader lnr = new LineNumberReader(new FileReader(_path));
		lnr.setLineNumber(1);
		StreamTokenizer stok = new StreamTokenizer(lnr);

		int[] dataLine = null;
		ArrayList array = new ArrayList();


		while ((dataLine = readLine(stok)) != null)
		{
			if (dataLine.length < 3)
				continue;
			Literal[] literals = new Literal[3];
			for (int i = 0; i < 3; i++)
			{
				//if (dataLine[i] > 0)
				//    dataLine[i]--;
				//else
				//    dataLine[i]++;
				literals[i] = new Literal(dataLine[i]);				/////////////////////				
			}
				
			Clause newClause = new Clause(literals);

			array.add(newClause);
		}

		Clause[] result = new Clause[array.size()];
		array.toArray(result);
		return result;
	}

	private int[] readLine(StreamTokenizer stok) throws IOException
	{
		ArrayList res = new ArrayList();
		double sum = 0;
		int[] result = null;
		boolean comment = false;
		stok.parseNumbers();
		stok.eolIsSignificant(true);
		stok.commentChar(35);

		//if(stok.ttype == StreamTokenizer.TT_EOF) return null;
		while (sum == 0)
		{
			stok.nextToken();

			while (stok.ttype != StreamTokenizer.TT_EOL && stok.ttype != StreamTokenizer.TT_EOF)
			{
				if (stok.ttype == StreamTokenizer.TT_NUMBER)
				{
					res.add(new Integer((int)stok.nval));
					sum++;//= stok.nval;
				}
				if (stok.ttype == StreamTokenizer.TT_WORD)
				{
					comment = true;
					break;
				}
				
				stok.nextToken();
			}
			if (stok.ttype == StreamTokenizer.TT_EOF)
			{
				if (sum != 0) break;
				else return null;
			}
		}
		if (comment)
			return new int[] { };
		result = new int[res.size()];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = ((Integer)res.get(i)).intValue();
		}

		return result;
	}

//        public int loadNumberLiterals() {
//        String countLit1 = _path.substring(7, 9);
//        String countLit2 = _path.substring(7, 10);
//        int number;
//
//        try {
//            number = Integer.parseInt(countLit1);
//        } catch (Exception e) {
//            number = Integer.parseInt(countLit2);
//        }
//
//        return number;
//    }
}
