const {createApp} = Vue


const app = createApp({
    data(){
        return {
            backEnd:[],
            clients: {},
            client: [],
            clientsJson: {},
            firstName: "",
            lastName: "",
            email:"",
            
            clientSelect: {},
            urlclientSelect: "",

            // darkMode
            checked: true,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            bgGrey: ""
        }
    },
    created(){
        this.loadData()
    },
    methods: {
        loadData(){
            axios
            .get("http://localhost:8080/api/clients")
            .then((data) =>{
                this.backEnd = data.data
                
                this.clients = data.data
                console.log(this.clients) 
                console.log(this.backEnd)
                this.clientsJson = data.data
            })
            .catch((error) => console.log(error))
            

        },
        addClient(){
            this.client = {
                firstName: this.firstName,
                lastName: this.lastName,
                email: this.email
            }
            if(this.firstName != "" && this.lastName != "" && this.email != "" && this.email.includes("@" && ".")){
                this.postClient(this.client)
                this.firstName = ''
                this.lastName = ''
                this.email = ''
            } else {
                Swal.fire({
                    icon: "error",
                    title: `Email: ${this.email} is not correct`,
                    text: 'Please correct the email format "@" and "." ',
                })
            }
        },
        postClient(client){
            axios.post("http://localhost:8080/rest/clients", client)
            .then(client => this.loadData())
        },
        selectedCustomer(client) {
            this.clientSelect = client;
            this.urlclientSelect = "/rest/clients/" + client.id;
        },
        deleteClient(url){
            this.clientSelect.accountDTO.forEach(account => {
                
                axios.delete("/rest/accounts/" + account.id)
            })

            function saludos() {
                axios.delete(url)
               

                Swal.fire({
                    icon: 'success',
                    title: 'Accounts successfully deleted',
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.reload()
                    }
                })
            }

            setTimeout(saludos, 100);
        },
        async editClient(client){
            let getId = client._links.client.href.lastIndexOf("/")
            let id = client._links.client.href.substring(getId + 1)
            console.log(id)
            const { value: formValues } = await Swal.fire({
                title: 'Multiple inputs',
                html:
                  `<input id="swal-input1" placeholder="${client.firstName}" class="swal2-input">` +
                  `<input id="swal-input2" placeholder="${client.lastName}" class="swal2-input">`+
                  `<input id="swal-input3" placeholder="${client.email}" class="swal2-input">`, 
                focusConfirm: false,
                preConfirm: () => {
                  return [
                    document.getElementById('swal-input1').value,  
                    document.getElementById('swal-input2').value, 
                    document.getElementById('swal-input3').value 
                  ]
                }
              })
              let editName = document.getElementById("swal-input1").value;
              let editLastName = document.getElementById("swal-input2").value;
              let editEmail = document.getElementById("swal-input3").value;
              if (formValues) {
                
                this.client = {
                    firstName: editName,
                    lastName: editLastName,
                    email: editEmail
                }
                if(editName != "" && editLastName != "" && editEmail != "" ){
                    if (editEmail.includes(".") && editEmail.includes("@")) {
						axios.put(`http://localhost:8080/rest/clients/${id}`, this.client)
                        .then((response) => this.loadData())
						Swal.fire({
							icon: "success",
							title: "The client has been edited",
						})
					} else {
						Swal.fire({
							icon: "error",
							title: `Email: ${editEmail} is not correct`,
							text: 'Please correct the email format "@" and "." ',
						})
					}
                    
                } else {
                    Swal.fire({
						icon: "error",
						title: "Fields are empty",
						text: "Please complete the fields",
					})
                }
                
              }
        }
        

    },
    computed: {
        darkMode(){
            if(this.checked){
                this.currentText = "text-white"
                this.currentBackGround = "bg-dark"
                this.mode = "Dark Mode"
                this.bgGrey = "backgroundScroll"
            }else{
                this.currentText = "text-black"
                this.currentBackGround = "bg-light"
                this.mode = "Ligth Mode"
                this.boxShadow = "m-3 shadow p-3 mb-5 bg-body rounded"
                this.bgGrey = "bg-light"
            }
            
        }
    }
})


app.mount('#app')