Imports System.Net
Imports System.Net.Sockets
Imports System.Threading
Class Form

    Dim port As String
    Dim ip As String
    Dim username As String
    Dim socket As Socket
    Private thread As Thread
    Dim messages As String = ""


    Private Sub TextBox3_TextChanged(sender As Object, e As EventArgs) Handles TextPort.TextChanged

    End Sub

    Private Sub ButtonConnect_Click(sender As Object, e As EventArgs) Handles ButtonConnect.Click
        If ButtonConnect.Text = "Connect" Then
            port = TextPort.Text
            ip = TextIP.Text
            username = TextUsername.Text

            Try
                Dim bytes(255) As Byte
                socket = New Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp)
                socket.Connect(New IPEndPoint(IPAddress.Parse(ip), port))

                Dim bytesCount As Integer = socket.Receive(bytes)
                If String.Compare(System.Text.Encoding.ASCII.GetString(bytes), "Username :") Then
                    socket.Send(System.Text.Encoding.ASCII.GetBytes(username & vbCrLf))
                    thread = New Thread(AddressOf Receive)
                    thread.IsBackground = True
                    thread.Start()
                Else
                    MsgBox("Error")
                    Return
                End If
            Catch ex As Exception
                MsgBox(ex.ToString)

                MsgBox("Error")
                Return
            End Try

            ButtonConnect.Text = "Disconnect"
            TextPort.Enabled = False
            TextIP.Enabled = False
            TextUsername.Enabled = False
            TextChat.Enabled = True
            ButtonSend.Enabled = True
        Else

            thread.Abort()
            socket.Close()
            ButtonConnect.Text = "Connect"
            TextPort.Enabled = True
            TextIP.Enabled = True
            TextUsername.Enabled = True
            TextChat.Enabled = False
            ButtonSend.Enabled = False
        End If



    End Sub

    Private Sub ButtonSend_Click(sender As Object, e As EventArgs) Handles ButtonSend.Click
        If Not (TextChat.Text = "") Then
            Dim bytes As Byte() = System.Text.Encoding.ASCII.GetBytes(TextChat.Text & vbCrLf)
            Dim BytesEnvoyes As Integer = socket.Send(bytes)
            TextChat.Text = ""
        End If
    End Sub

    Public Sub putMessage(ByVal TextMessages As TextBox, ByVal message As String)
        messages = messages & message
        If TextMessages.InvokeRequired Then
            TextMessages.Invoke(Sub() putMessage(TextMessages, messages))
        Else
            TextMessages.Text = messages
        End If

    End Sub

    Private Sub Receive()
        While (True)
            Dim bytes(255) As Byte
            Dim bytesCount As Integer = socket.Receive(bytes)
            Dim i As String = System.Text.Encoding.ASCII.GetString(bytes)

            putMessage(TextMessages, System.Text.Encoding.ASCII.GetString(bytes))
        End While
    End Sub
End Class
