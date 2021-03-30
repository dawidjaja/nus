# Exercise 9

## A0184588J - Daniel Alfred Widjaja

I timed both mutex and semaphores and got 0.004s and 0.007 time respectively. I feel that this could not be a fair comparison since the there is some implementation that is different from both codes. For example, mutex has conditional variable which helps to wake up a thread after a certain condition is fulfilled. However this is not applicable in semaphores. I also think that since semaphore play with shared memory, it will take longer time to actually read and write to variable. Which will cause the program to run in a longer time. 