
# Setup
Make sure to be in the main project folder!  
### Add correct paths
vim sql/src/load_data.sql  
modify this file to put the absolute paths according to your local computer.  

### Start and initialize the database
source ./sql/scripts/startPostgreSQL.sh  
pg_ctl status (This should say "server is running". If it doesn't, then there was a problem with the previous line)  
source ./sql/scripts/create_and_initialize_db.sh  

### Run the Java interface
source ./java/scripts/compile.sh  

# Remember to stop postgre when finished!
source ./sql/scripts/stopPostgreDB.sh


# developing
(From the main project folder):  
vim java/src/Cafe.java  


# Link to project report:
https://docs.google.com/document/d/1aAUJ2mOJTR30KvO_vwhtC1DQEuvZsoLg-j2Cci6cVbo/edit?usp=sharing  











