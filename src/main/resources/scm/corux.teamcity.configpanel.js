Ext.ns('corux.teamcity');

corux.teamcity.ConfigPanel = Ext.extend(Sonia.repository.PropertiesFormPanel, {
  formTitleText : 'TeamCity',
  urlText : 'Url',
  urlHelpText : 'Url of teamcity installation (e.g. http://teamcity.example.com/).',

  vcsRootText : 'VCS Root',
  vcsRootHelpText : 'ID of the TeamCity VCS root.',

  usernameText : 'Username',
  usernameHelpText : 'Username for TeamCity.',

  passwordText : 'Password',
  passwordHelpText : 'Password for TeamCity.',

  initComponent : function() {
    var config = {
      title : this.formTitleText,
      items : [ {
        name : 'teamcityUrl',
        fieldLabel : this.urlText,
        property : 'teamcity.url',
        vtype : 'url',
        helpText : this.urlHelpText
      }, {
        name: 'teamcityUsername',
        fieldLabel : this.usernameText,
        property : 'teamcity.username',
        helpText : this.usernameHelpText
      }, {
        name: 'teamcityPassword',
        inputType: 'password',
        fieldLabel : this.passwordText,
        property : 'teamcity.password',
        helpText : this.passwordHelpText
      }, {
        name : 'teamcityVcsRoot',
        fieldLabel : this.vcsRootText,
        property : 'teamcity.vcsroot',
        helpText : this.vcsRootHelpText
      } ]
    };

    Ext.apply(this, Ext.apply(this.initialConfig, config));
    corux.teamcity.ConfigPanel.superclass.initComponent.apply(this, arguments);
  }
});

Ext.reg('teamcityConfigPanel', corux.teamcity.ConfigPanel);

Sonia.repository.openListeners.push(function(repository, panels) {
  if (Sonia.repository.isOwner(repository)) {
    panels.push({
      xtype : 'teamcityConfigPanel',
      item : repository
    });
  }
});
