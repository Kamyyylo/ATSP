# Asymmetric Travelling Salesman Problem

Whole code is in 5 Java classes:<br />
*-DPandBF.java*<br />
*-Main*<br />
*-DPandBFPane.fxml*<br /><br />
*-TabuSearch.java*<br /><br />
*-TabuSearchPane.fxml*<br /><br />
Data input is taken from files<br />
**Examplary file (First number is the size of the matrix):**<br /><br />
6<br />
 0 20 30 31 28 40<br />
30  0 10 14 20 44<br />
40 20  0 10 22 50<br />
41 24 20  0 14 42<br />
38 30 32 24  0 28<br />
50 54 60 52 38  0<br /><br />
To solve ATSP i used **Dynamic Programming**, **Brute-Force method** and **TabuSeach(To find closest value for many cities)**

**TabuSearch** has 3 definition of neighborhood and diversification(criticalEvent).


Program has userfriendly interface to test and learn this two algorithms:

![alt text](https://i.ibb.co/rtNCb7S/Screenshot-2018-12-06-21-53-39.jpg)

![alt text](https://i.ibb.co/7RmcxQz/Screenshot-2018-12-06-21-53-51.jpg)




