#*GPS Master Breakdown*

## Why bother make another GPS file analyzer? 
- Modern GPS based apps are great, but there is a lot they don't do. In particular for runners such as 
  myself they don't tell me **how far/fast I've gone for short time/distance interval sessions**. 
 
  My plan for this project is to first implement a very basic GPS analyzer and then extend it to be able
  to determine what kind of session the runner did by only analyzing the .GPX file. If a runner does a 
  10x400m sprint training session the program will detect this and give the time splits for the 400m sets
  and the avg times across the splits.
  
  Likewise if a runner does 10x2mins sprint intervals the program will also detect this and tell the user how
  far they went over all the individual splits and the average distance they traveled. 
  
  However first I need to get all the basic functionality working, that is, get the Km/Mile splits and average speeds etc.

  The end goal for this project, however, is to implement accurate short distance interval training analysis. 
