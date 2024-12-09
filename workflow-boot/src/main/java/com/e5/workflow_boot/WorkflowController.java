package com.e5.workflow_boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WorkflowController {

    @Autowired
    private ApplicationContext applicationRestart;

    private boolean data = true;

    @PostMapping(value = {"/changeData"})
    public ResponseEntity<String> changeData(){
        this.data = false;
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping(value = {"/deploymentId/{value}"})
    public ResponseEntity<Boolean> getHealth(@PathVariable String value) {
        System.out.println("Received value: " + value);
        // Use the value in your logic
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping(value = {"/restart"})
    public ResponseEntity<String> restartService(){
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Optional delay for graceful shutdown
                ((ConfigurableApplicationContext) applicationRestart).close();
                SpringApplication.run(WorkflowBootApplication.class); // Replace Application.class with your main class
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        thread.setDaemon(false); // Ensure the thread does not terminate prematurely
        thread.start();;
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
