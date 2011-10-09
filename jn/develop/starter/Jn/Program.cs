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
        static String XMS = "-Xms512m";
        static String XMX = "-Xmx512m";

        [STAThread]
        static void Main()
        {
            Process p = Process.GetCurrentProcess();

            ProcessStartInfo startInfo = new ProcessStartInfo
                                             {
                                                 FileName = "javaw",
                                                 Arguments = p.StartInfo.Arguments + " -cp ./libs/*; com.jds.jn.Jn"
                                             };
            Process.Start(startInfo);
        }
    }
}
