# Description

This folder contains [Jenkinsfile Runner](https://github.com/jenkinsci/jenkinsfile-runner) for local testing of Jenkinsfile, a compatible version of [Jenkins 2.375 LTS](https://get.jenkins.io/war-stable/2.375.1/) and plugins compatible with this version.

## Check with

PS C:\Program Files\Jenkins\jenkinsfile-runner-1.0-beta-32\bin> .\jenkinsfile-runner.bat `
>>   --jenkins-war "C:\Program Files\Jenkins\jenkinsfile-runner-1.0-beta-32\jenkins.war" `
>>   --plugins "C:\Program Files\Jenkins\plugins" `
>>   --file "C:\Users\Roman\AndroidStudioProjects\SWAPI\Jenkinsfile"

## Result: 

Started
Resume disabled by user, switching to high-performance, low-durability mode.
[Pipeline] Start of Pipeline
[Pipeline] End of Pipeline
Finished: SUCCESS