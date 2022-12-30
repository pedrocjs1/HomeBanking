const {createApp} = Vue

const transaction = createApp({
    data(){
        return {
            cards:[],
            clientsAccount: {},
            loans: [],
            clientLoans: {},
            balanceTotalLoan: "",
            
            selectType: "ownAccount",
            selectOwner:"asdasd",

            accountOrigin: "",
            accountTarget:"",
            accountTargetOther: "",

            description: "",

            accountOwn: [],

            username: "",
            

            checkedAccount: undefined,
            currentText: "",
            currentBackGround: "",
            mode: "",
            boxShadow: "",
            backgroundBody: "",
            backgroundTitle: "",
            shadowCard: "",
            bgGrey: "",
            backgroundLogo: "",

            checkedHeader: true,
            header: "",
            headerDesktop: "",

            balanceTotal: "",

            view: true,

            viewPassword: "d-none",
            noViewPassword: "",


            amount: 0,
            
            
            
            
        }
    },
    created(){
        this.loadData()
        this.checkedAccount = localStorage.getItem("mode") 
    },
    methods: {
        loadData(){
            axios
            .get("http://localhost:8080/api/clients/current")
            .then((data) =>{
                this.cards = data.data.cards.sort((a,b) => a.id - b.id)
                this.username = data.data.firstName + ' ' + data.data.lastName
                // this.clientsAccount = this.accountClient.sort((a,b) => a.id - b.id)
                // this.balanceTotal = this.clientsAccount.map(account => account.balance).reduce((iter, acc) => iter + acc).toFixed(2)
                
                this.accountOwn = data.data.accountDTO.sort((a,b) => a.id - b.id)
                console.log(this.accountOwn)
                this.accountOrigin = [... data.data.accountDTO.map(number => number.number)][0]
                this.accountTarget = data.data.accountDTO.length > 1 ? data.data.accountDTO[1].number : "No other account"
                console.log(this.accountOrigin)
                
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
        logout() {
            axios.post('/api/logout').then(response => {
                
                window.location.href = './index.html'
            })
        },
        createCard() {
            axios
                    .post(
                        "/api/clients/current/cards",
                        `colorCard=${this.selectColor}&cardType=${this.selectType}`
                    )
                    .then((response) => window.location.href=("./cards.html"))

                    .catch((error) => Swal.fire({
                        icon: "error",
                        title: "Oops...",
                        text: `${error.response.data}`,
                    }));
        },
        updateAmount(event) {
            this.amount = event.target.value;
        },
        swapValues() {
            const temp = this.accountOrigin;
            this.accountOrigin = this.accountTarget;
            this.accountTarget = temp;
        }, 
        transaction() {

            Swal.fire({
                title: 'Do you make transaction?',
                showDenyButton: true,
                // showCancelButton: true,
                confirmButtonText: 'Accept',
                denyButtonText: `Cancel`,
            }).then((result) => {

                if (result.isConfirmed) {
                    if (this.amount > 0 && this.description != "" && this.selectType === "ownAccount") {

                        axios.post('/api/transactions', `description=${this.description}&amount=${this.amount}&originAccountNumber=${this.accountOrigin}&targetAccountNumber=${this.accountTarget}`)
                            .then(response => {
                                Swal.fire('Transaction Success', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {

                                this.error = error.response.data
                                Swal.fire('Transaction Failed', this.error, 'error')
                                    .then(result => {
                                        window.location.reload()
                                })
                            })
                    } else if (this.amount > 0 && this.description != "" && this.selectType === "thirdAccount"){
                        axios.post('/api/transactions', `description=${this.description}&amount=${this.amount}&originAccountNumber=${this.accountOrigin}&targetAccountNumber=${this.accountTargetOther}`)
                            .then(response => {
                                Swal.fire('Transaction Success', '', 'success')
                                    .then(result => {
                                        window.location.reload()
                                    })
                            }).catch(error => {

                                this.error = error.response.data
                                Swal.fire('Transaction Failed', this.error, 'error')
                                    .then(result => {
                                        window.location.reload()
                                })
                            })
                    } else {
                        Swal.fire('Transaction Failed', 'Complete all fields')
                                    .then(result => {
                                        window.location.reload()
                                    })

                    }
                    
                    
                    
                } else if (result.isDenied) {
                    Swal.fire('Cancel transaction', '', 'error')
                }
            })
    
        }
       
        
        
            
    },
    computed: {
      darkMode(){
        if (this.checkedAccount === "true" || this.checkedAccount === true) {
          this.currentText = "text-white"
          this.currentBackGround = "bg-dark"
          this.mode = "Dark Mode"
          this.backgroundBody = "backgroundBody"
          this.backgroundTitle = "bg-dark"
          this.shadowCard = "shadowWhite" 
          this.bgGrey = "bg-light borderRadius"
          this.backgroundLogo = ""
          localStorage.setItem("mode", true)

        } else if (this.checkedAccount === "false" || this.checkedAccount === false) {
          this.currentText = "text-black"
          this.currentBackGround = "backGroundGrey"
          this.mode = "Ligth Mode"
          this.boxShadow = "m-3 shadow bg-body rounded"
          this.backgroundBody = "backgroundWhite "
          this.backgroundTitle = "backgroundTitletarget"
          this.shadowCard = "shadow  bg-body rounded" 
          this.bgGrey = "backgroundBodyCard borderRadius"
          this.backgroundLogo = ""
          localStorage.setItem("mode", false)
        }
    },
    maxAmount() {
        const originAccount = this.accountOwn.find((a) => a.number === this.accountOrigin);
        if (originAccount) {
          return originAccount.balance;
        } else {
          return 0;
        }
    },
    originAccountAmount() {
        return `${this.amount}`;
    },
    

    
    
    
    }
})


transaction.mount('#transfers')
