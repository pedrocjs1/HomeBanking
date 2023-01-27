const {createApp} = Vue

const account = createApp({
    data(){
        return {
            accountInfo:[],
            account: [],
            accountTransaction: [],
            accountName: "",
            
            username: "",
            accountBalance: "",
            

            checkedAccount: undefined,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            backgroundBody: "",
            backgroundLogo: "",

            checkedHeader: true,
            header: "",
            headerDesktop: "",
            id: new URLSearchParams(location.search).get("id"),
            idAccount: 0,

            dateFrom: "",
            dateTo: "",
            accountNumber: "",
            

            
            
            
        }
    },
    created(){
        this.loadData()
        this.checkedAccount = localStorage.getItem("mode") 
    },
    methods: {
        loadData(){
            axios
            .get("/api/clients/current")
            .then((data) =>{
                this.accountInfo = data.data.accountDTO

                this.account = [... data.data.accountDTO].find(element => element.id == this.id)
                this.accountTransaction = this.account.transactions.sort((a,b) => b.id - a.id)
                this.accountName = this.account.number
                this.accountBalance = this.account.balance
                this.idAccount = this.account.id
                this.username = data.data.firstName + ' ' + data.data.lastName
                this.accountNumber = this.account.number
                
                console.log(this.accountInfo);
                   
            })
            .catch((error) => console.log(error))

            
        },
        collapseHeaderMovile(){
            if (this.header == "mob-menu-opened"){
                this.header = ""
            } else {
                this.header = "mob-menu-opened"
            }
        },
        formatedDate(dateInput) {
            const date = new Date(dateInput)
            return date.toDateString().slice(3)
        },
        formatedHour(dateInput) {
            const date = new Date(dateInput)
            let minutes = date.getMinutes() > 9 ? date.getMinutes() : "0" + date.getMinutes()
            return date.getHours() + ":" + minutes
        },
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        addCommas(number) {
            return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        },
        disableAccount() {
            Swal.fire({
                    title: '¿Disable Account?',
                    showDenyButton: true,
                    // showCancelButton: true,
                    confirmButtonText: 'Accept',
                    denyButtonText: `Cancel`,
                })
                .then((result) => {
                    if (result.isConfirmed) {
                        Swal.fire({
                                input: 'password',
                                inputLabel: 'Enter your password for confirm',
                                inputValue: '',

                                showCancelButton: true,
                                confirmButtonText: 'Confirm',
                            })
                            .then(result => {
                                if (result.isConfirmed) {

                                    axios.patch('/api/clients/current/accounts/disabled', `idAccount=${this.idAccount}&password=${result.value}`)
                                        .then(response => {
                                            Swal.fire('Account deactivated', '', 'success')
                                                .then(result => {
                                                    window.location.href=("./accounts.html")
                                                })
                                        }).catch(error => {
                                            this.error = error.response.data
                                            Swal.fire('Failed deactived account', this.error, 'error')
                                            
                                        })
                                }
                            })
                    }
                })
        },
        downloadPDF() {
            axios({
              method: 'post',
              url: '/api/transactions/pdf',
              data: {
                dateFrom: this.dateFrom,
                dateTo: this.dateTo,
                accountNumber: this.accountNumber
              },
              responseType: 'blob' // indica que la respuesta es un archivo
            })
            .then(response => {
              const url = window.URL.createObjectURL(new Blob([response.data]));
              
              const link = document.createElement('a');
              
              link.href = url;
              link.setAttribute('download', 'TransactionInfo.pdf');
              document.body.appendChild(link);
              link.click();
              Swal.fire('Pdf Downloaded', '', 'success')
                  .then(result => {
                      window.location.reload()
                  })
            })
            .catch(error => {
              this.error = error.response.data
              Swal.fire('Failed download PDF', this.error, 'error')
            });
          },
        
        
            
    },
    computed: {
      darkMode(){
        if (this.checkedAccount === "true" || this.checkedAccount === true) {
          this.currentText = "text-white"
          this.currentBackGround = "bg-dark"
          this.mode = "Dark Mode"
          this.backgroundBody = "backgroundBody"
          this.backgroundLogo = ""
          localStorage.setItem("mode", true)

        } else if (this.checkedAccount === "false" || this.checkedAccount === false) {
          this.currentText = "text-black"
          this.currentBackGround = "backGroundGrey"
          this.mode = "Ligth Mode"
          this.boxShadow = "m-3 shadow p-3 mb-5 bg-body rounded"
          this.backgroundBody = "backgroundWhite "
          this.backgroundLogo = ""
          localStorage.setItem("mode", false)
        }

    },
    
    
    
    }
})


account.mount('#account')