# CronSchedulerII
* **Adopted Solution:** The solution basically consists of three main entities. The smallest entity is a _Callable_ entity representing the script or function
 that should be run and returns the exit code of the process being run. Another bigger entity is _Worker_ which represents the _Runnable Thread_ for a single job/ task and it is responsible for handling timeouts, job execution
and frequently executing the job. The largest entity is the _Scheduler_ which is responsible for creating a distinct worker for each job and runs the workers _(Threads)_ Concurrently. 

* **Reasonings:** Since the task requires schduling and running jobs, there was no escape from creating threads. Thus _Runnable_ Interface was used to create threads. In the _Runnable_ Class, I wanted to take care of jobs timeouts, therefore, the job class implemnted _Callable_ so 
that the call of the job can be used in a "Promise-like" manner to terminate the job if it exceeded the expected exectuion interval. The Scheduler takes the job as a csv input for simplicity. Jobs are windows executable bash/ batch scripts for simplicity.  

* **Tradeoffs:** NONE
* **Snippets:** Passing the jobs path to constructor of the scheduler. ![image](https://github.com/JrElaraby/CronSchedulerII/assets/64870535/82baff53-435b-46c6-8f75-0e417a55e662) <br />
In the runnable class. Job is called frequently and if it exceeded the expected interval, thread is terminated. ![image](https://github.com/JrElaraby/CronSchedulerII/assets/64870535/7dc8329e-04f8-43b8-a25f-59a4819a4e2b)<br />
  Process exit code is returned after the execution. -1 is returned in case of exception thrown ![image](https://github.com/JrElaraby/CronSchedulerII/assets/64870535/82c4b5fa-a923-4dc0-9f28-d8db96404421) <br />
Scheduler creating different thread for each worker. ![image](https://github.com/JrElaraby/CronSchedulerII/assets/64870535/f588712c-6725-4bb0-93cb-7a50d965113e)







* **Possible future improvements:**
  Instead of taking jobs as CSV file, we can retrive them from a specific source like calling the source and get the jobs as a response. Run different types of jobs instead of executable bash/ batch scripts. It can be more generic. we can allow different scheduling techniques.



----------------------------------------------------
* **Important Note** Any path passed should be raltive to _MyScheduler.java_. Scripts should be bash/ btach executable scripts. Jobs given as an input in a csv file in the format (Id, expected Interval, Scheduling frequency, path to script(relative to MyScheduler.java)). CSV files should be passed without headers. First line represents first job.  
