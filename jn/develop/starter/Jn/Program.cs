using System;
using System.Collections.Generic;
using System.Windows.Forms;
using System.Diagnostics;
using System.Xml;
using System.Collections.Specialized;
using System.Configuration;

namespace Jn
{
    static class Program
    {
        static String JAVA_HOME = null;
        static String XMS = "-Xms512m";
        static String XMX = "-Xmx512m";

        [STAThread]
        static void Main()
        {
            load();
            if (JAVA_HOME == null)
                return;

            ProcessStartInfo startInfo = new ProcessStartInfo();
            startInfo.FileName = JAVA_HOME + "javaw.exe";
            startInfo.Arguments = XMS + " " + XMX + " " + " -cp ./libs/*; jds.jn.Jn";
            Process.Start(startInfo);
        }
       
        static void load()
        {
            XmlDocument doc = new XmlDocument();
            doc.Load("settings.xml");

            XmlNode xNode = doc.GetElementsByTagName("settings").Item(0);

            IConfigurationSectionHandler csh = new NameValueSectionHandler();
            NameValueCollection nvc = (NameValueCollection)csh.Create(null, null, xNode);

            JAVA_HOME = nvc.Get("JAVA_HOME");
            XMS = nvc.Get("XMS");
            XMX = nvc.Get("XMX");
        }  

    }
}
