#!/usr/bin/perl

#This file generates test cases for Herbert that you can use to test your program.
#Careful: produces a file of size (on average) 9*$employers*$maxtime bytes!
#You can accidentally produce hundreds of MB if you are not careful, be warned

#Herbert will work for $employers employers
$employers=10000;

#He works for each for up to $maxtime minutes.
#Note: maxtime < 90000 or bad things will happen
$maxtime=100;


#replace this by whatever number you like
srand 66012222;
for($i=0;$i<$employers;$i++){                  
    $k=rand()*$maxtime;
    #$k=$maxtime;
    $k=($k<1)?1:$k;
    for($j=0;$j<$k;$j++){
        printf("%010d:%d:\n",$i,10000+$j)
    }
}
