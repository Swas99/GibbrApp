package com.swastik.gibbrtestapp.modules.helper;

/**
 * Created by Swastik on 15-06-2016.
 */
public class MissingWord
{
    public int startIndex;
    public int endIndex;
    public String wordType;
    public String userInput;

    @Override
    public String toString()
    {
        return String.valueOf(startIndex);
    }
}
