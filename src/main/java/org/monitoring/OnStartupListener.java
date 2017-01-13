package org.monitoring;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import org.monitoring.jobs.SchedulesJob;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

@WebListener
public class OnStartupListener implements ServletContextListener {
    
    private Scheduler scheduler = null;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            JobDetail job = newJob(SchedulesJob.class).withIdentity("SchedulesJob").build();

            Trigger trigger = newTrigger()
                    .withIdentity("triggerSchedulesJob")
                    .startNow()
                    .withSchedule(
                            simpleSchedule()
                            .withIntervalInMinutes(1)
                            .repeatForever()
                    )
                    .build();

            scheduler.scheduleJob(job, trigger);
            
            System.out.println("Cron de schedules iniciado correctamente");
        } catch (SchedulerException ex) {
            System.out.println("Ocurrio un error al intentar iniciar el cron de schedules [" + ex.toString() + "]");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("App stopped");
        try {
            if(scheduler != null){
                scheduler.shutdown();
                System.out.println("Cron de schedules detenido correctamente");
            }
        } catch (SchedulerException ex) {
            System.out.println("Ocurrio unproblema al detener el Cron de schedules");
        }
    }

}
