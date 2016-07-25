Ext.ns('corux.teamcity');

corux.teamcity.ConfigPanel = Ext.extend(Sonia.repository.PropertiesFormPanel, {
  formTitleText : 'TeamCity',
  urlText : 'Url',
  vcsRootText : 'VCS Root',

  urlHelpText : 'Url of teamcity installation (e.g. https://teamcity.yourserver.local/).',
  vcsRootHelpText : 'ID of the TeamCity VCS root.',

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
        name : 'teamcityVcsRoot',
        fieldLabel : this.VcsRootText,
        property : 'teamcity.vcsroot',
        helpText : this.VcsRootHelpText
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
