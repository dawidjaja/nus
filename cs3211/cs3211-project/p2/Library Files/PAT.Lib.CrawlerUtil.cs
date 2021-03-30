using System;
using System.Collections.Generic;
using System.Text;

//the namespace must be PAT.Lib, the class and method names can be arbitrary
namespace PAT.Lib
{
    public class CrawlerUtil
    {
        public static int forLoopNext(int[] arr, int row, int cols)
        {
            for (int i = 0; i < cols; i++) {
            	if (arr[row * cols + i] == 1) {
            		return i;
            	}
            }
            
            return -1;
        }
        
        public static int count(int[] arr, int row, int cols)
        {
        	int count = 0;
            for (int i = 0; i < cols; i++) {
            	if (arr[row * cols + i] == 1) {
            		count++;
            	}
            }
            
            return count;
        }
    }
}
