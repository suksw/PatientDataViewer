# PatientDataViewer

An application that requests data from
https://fhirtest.uhn.ca/read?serverId=home_21&pretty=true&resource=Patient&action=read&id=202967&vid=4
and stores as JSON files in PatientDataViewer/src/main/resources/.  
  
Patient info are stored without any changes, and Encounters, Observations are stored by Patient ids (for Encounters) and Encounter ids (for Observations). 

### Resources Accessed
http://hapi.fhir.org/baseDstu3/Patient  
http://hapi.fhir.org/baseDstu3/Encounter  
http://hapi.fhir.org/baseDstu3/Observation  
