# Android Project - Group 36

- This ReadMe file contains the details of the project, how to build/execute the app, and notes on GenAI use. 

## Building and Running the App
- Since this app was done natively without importing any libraries, this should not be a difficult process.

1. Start by cloning the repository. 
```bash

git clone https://github.com/ayushp0728/Android36.git

```

2. Afterwards, install all required dependencies and ensure that you have the correct emulator set. 

#### Please note that due to prior issues, we had to restart our project and rename it to Android36Java internally. So, copying over the files will likely create some kind of break if you are expecting Android<group_number>. Sorry for the trouble. 



## Notes on Gen AI Usage
- In our experience, AI is most competent at assisting the user with mundane tasks and grasping a concept conceptually rather than code generation. We've tried using it for code generation in the past, but it has caused some issues. 

### Prompts Issued to GenAI

- “Do you remember the photos app I made last week using JavaFX? Today, I need to work on a project where I’m porting that app to Android. How would I go about that?”
- “How do I start porting the model layer?”
- “How do I implement the home screen?”
- “Can you explain what the assignment requirements mean?”
- “What is the best way to split work between two people?”
- “What are the required screens?”
- “How do I handle Android content URIs?”
- “How do I persist URI permissions?”
- “How do I open PhotoViewActivity from AlbumActivity?”
- "How would I go about creating a prefix-based search feature"

### There were cases where Gen AI (we used ChatGPT) was able to provide details on implementation, and generated code within examples. This code was provided in the: 
- RecyclerView
- Activity
- Click Listeners
- Dialog
- Method stubs

### We did not issue any UI or drawings to the model. These features were implemented by us: 

- All Android XML layouts
- Activity lifecycle logic
- RecyclerView setup
- URI persistence code
- navigation logic
- rename/delete album logic
- delete/move photo logic