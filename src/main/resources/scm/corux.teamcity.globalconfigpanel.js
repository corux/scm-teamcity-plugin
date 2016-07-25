Ext.ns('corux.teamcity');

corux.teamcity.GlobalConfigPanel = Ext
    .extend(
        Sonia.config.ConfigForm,
        {
          titleText : 'TeamCity Configuration',

          urlText : 'Url',
          urlHelpText : 'Url of TeamCity installation (e.g. https://teamcity.yourserver.local/).',

          usernameText : 'Username',
          usernameHelpText : 'Username for TeamCity.',

          passwordText : 'Password',
          passwordHelpText : 'Password for TeamCity.',

          useRepositoryNameAsDefaultText : 'Use repository name if not set',
          useRepositoryNameAsDefaultHelpText : 'Uses the SCM repository name as TeamCity VCS root, if no custom name is defined.',

          initComponent : function() {
            var config = {
              title : this.titleText,
              items : [ {
                xtype : 'textfield',
                fieldLabel : this.urlText,
                name : 'url',
                vtype : 'url',
                allowBlank : true,
                helpText : this.urlHelpText
              }, {
                xtype : 'textfield',
                fieldLabel : this.usernameText,
                name : 'username',
                allowBlank : true,
                helpText : this.usernameHelpText
              }, {
                xtype : 'textfield',
                inputType: 'password',
                fieldLabel : this.passwordText,
                name : 'password',
                allowBlank : true,
                helpText : this.passwordHelpText
              }, {
                xtype : 'checkbox',
                fieldLabel : this.useRepositoryNameAsDefaultText,
                name : 'use-repository-name-as-default',
                inputValue : 'true',
                helpText : this.useRepositoryNameAsDefaultHelpText
              } ]
            }

            Ext.apply(this, Ext.apply(this.initialConfig, config));
            corux.teamcity.GlobalConfigPanel.superclass.initComponent.apply(this, arguments);
          },

          onSubmit : function(values) {
            this.el.mask(this.submitText);
            Ext.Ajax.request({
              url : restUrl + 'plugins/teamcity/global-config.json',
              method : 'POST',
              jsonData : values,
              scope : this,
              disableCaching : true,
              success : function(response) {
                this.el.unmask();
              },
              failure : function() {
                this.el.unmask();
              }
            });
          },

          onLoad : function(el) {
            var tid = setTimeout(function() {
              el.mask(this.loadingText);
            }, 100);
            Ext.Ajax.request({
              url : restUrl + 'plugins/teamcity/global-config.json',
              method : 'GET',
              scope : this,
              disableCaching : true,
              success : function(response) {
                var obj = Ext.decode(response.responseText);
                this.load(obj);
                clearTimeout(tid);
                el.unmask();
              },
              failure : function() {
                el.unmask();
                clearTimeout(tid);
              }
            });
          }
        });

// register xtype
Ext.reg('teamcityGlobalConfigPanel', corux.teamcity.GlobalConfigPanel);

// register config panel
registerGeneralConfigPanel({
  id : 'teamcityGlobalConfigPanel',
  xtype : 'teamcityGlobalConfigPanel'
});
