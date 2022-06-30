/*******************************************************************************

* Licensed Materials - Property of IBM
*
* OpenPages GRC Platform (PID: 5725-D51)
*
* © Copyright IBM Corporation 2021 - CURRENT_YEAR. All Rights Reserved.
*
* US Government Users Restricted Rights- Use, duplication or
* disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
  *******************************************************************************/

This sample demonstrates how to build a React application using typescript, sass, and Carbon.

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

### Initial setup

Install nvm.

```nvm use```

This ensures you are using a compatible version on node and npm.

```npm i```

This installs all dependencies.

### Running the development server

*Set proxy*

Edit src/setupProxy.js and set "proxy" to the url of the OpenPages server. This is required in order for the dev server
to proxy api requests to OpenPages.

For example:

```const proxy = 'https://192.168.1.189:10111'```

If your OpenPages server is not deployed to root, you can add the context path to the URL.

```const proxy = 'https://192.168.1.189:10111/openpages'```

*Start dev server*

```REACT_APP_USERNAME=[username]  REACT_APP_PASSWORD=[password] npm run start```

Runs the app in the development mode.<br />
Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

The page will reload if you make edits.<br />
You will also see any lint errors in the console.

### Running production version inside OpenPages

Build the assets using this command:

```npm run build```

Copy the assets from the build folder to:

<OP_HOME>/wlp-usr/shared/apps/op-apps.ear/sosa.war/helper (for a traditional deployment)

/opt/ibm/wlp/usr/servers/defaultServer/apps/expanded/op-apps.ear/sosa.war/helper (for a CloudPak for Data deployment)

Note that the index.html asset for the helper must be directly underneath this folder and not in a subfolder.

### Link Field Integration

The helper can be launched inside of OpenPages by using a Link field, the path of field should point
to `/app/jspview/helper/`

Note that the trailing slash at the end of the path is required.

*Sample helper field configuration*

```
${
	"labelKey" : "pcm.creator.label.policyviewcomputedlink",
	"path" : "/app/jspview/helper/","modes" : ["view", "edit"],
	"parameters" :{
		"resourceId" : "$objectId"
	},
	"popUp" : {
		"windowAttributes" : "height=550,width=890,menubar=no,status=no,toolbar=no,scrollbars=yes,resizable=yes"
	}
}
```

### Examples

The examples component demonstrates how to use react router within a helper. Each example has it's own route which is
directly linkable.

**Tree**

Demonstrates how to integrate a 3rd party tree component, including multi selection and expand/collapse of nodes. Note
that Carbon now has a Tree component in the experimental stage. It is recommended to use Carbon component if it meets
your use case. The use of 3rd party component will likely be deprecated in the future.

**Application Text**

Demonstrates how to use the OpenPages public API to fetch localized application text and displays the results in a data
table. While the data is being fetched a placeholder skeleton is shown.

**Fields API**

*This example must be run from within OpenPages, launched from a url field, the dev server environment is not
supported.*

Demonstrates how to get and set task view field values from within a helper using the fields api.

The helper must be launched from within an OpenPages control view that contains the following fields:

```
System Fields:Name
System Fields:Description
OPSS-Ctl:Design Effectiveness
```

**Admin Mode**

This sample demontrates how to obtain the current status of system admin mode and demonstrates how to enable or disable
system admin mode. It requires an admin OpenPages user.

This sample uses a put request which automatically has the csrf token attached as a request parameter.

### Creating a .zip distribution

Update the version number in package.json

```npm pack```
